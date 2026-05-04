import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Compares two serialized game state JSON files for structural equivalence.
 *
 * The comparison rules are:
 * - Normal values (numbers, booleans, plain strings) require exact equality.
 * - ID values (any string that appears as the value of an "id" key in file 1,
 * or as an object key in an ID-keyed object like bank.accounts or
 * lane.vehicles)
 * are compared via a flexible mapping: when a file-1 ID is first encountered
 * it is paired with the corresponding file-2 value and the pairing is then
 * enforced consistently throughout the rest of the comparison.
 * - The "recoveryQueue" array is ordered: elements are compared positionally.
 * - All other arrays (players, vehicles, equipments, junctions, buildings,
 * lanes, stock, startingLaneIds, endingLaneIds, buildingIds, vehicleIds,
 * the per-lane vehicles array) are treated as unordered: the comparator
 * tries every pairing and backtracks on contradictions.
 */
public class StateComparator {

    // =========================================================================
    // Minimal JSON tree
    // =========================================================================

    /**
     * Base class for all JSON nodes.
     */
    abstract static class Node {
    }

    /**
     * Represents a JSON null value.
     */
    static class JNull extends Node {
        /** The singleton instance of JNull. */
        static final JNull I = new JNull();
    }

    /**
     * Represents a JSON boolean value.
     */
    static class JBool extends Node {
        /** The boolean value. */
        final boolean v;

        /**
         * Constructs a JBool node.
         *
         * @param v the boolean value
         */
        JBool(boolean v) {
            this.v = v;
        }
    }

    /**
     * Represents a JSON number value.
     */
    static class JNum extends Node {
        /** The double-precision floating-point value. */
        final double v;

        /**
         * Constructs a JNum node.
         *
         * @param v the double value
         */
        JNum(double v) {
            this.v = v;
        }
    }

    /**
     * Represents a JSON string value.
     */
    static class JStr extends Node {
        /** The string value. */
        final String v;

        /**
         * Constructs a JStr node.
         *
         * @param v the string value
         */
        JStr(String v) {
            this.v = v;
        }
    }

    /**
     * Represents a JSON array.
     */
    static class JArr extends Node {
        /** The list of nodes in the array. */
        final List<Node> elems;

        /**
         * Constructs a JArr node.
         *
         * @param e the list of nodes
         */
        JArr(List<Node> e) {
            elems = e;
        }
    }

    /**
     * Represents a JSON object.
     * keys and values maintain insertion order; keyOrder mirrors field order
     */
    static class JObj extends Node {
        /** The order of keys in the object. */
        final List<String> keyOrder;
        /** The map of field names to nodes. */
        final Map<String, Node> fields;

        /**
         * Constructs a JObj node.
         *
         * @param ko the list of keys in order
         * @param f  the map of fields
         */
        JObj(List<String> ko, Map<String, Node> f) {
            keyOrder = ko;
            fields = f;
        }
    }

    // =========================================================================
    // Comparator state
    // =========================================================================

    // IDs collected from file 1 (values of any "id" key).
    private final Set<String> knownIds = new HashSet<>();
    // Bidirectional mapping: file-1 ID -> file-2 ID
    private Map<String, String> fwdMap = new HashMap<>();
    // file-2 ID -> file-1 ID (prevents two file-1 IDs claiming the same file-2 ID)
    private Map<String, String> revMap = new HashMap<>();

    // =========================================================================
    // Public API
    // =========================================================================

    /**
     * Compares the two JSON files at the given paths.
     *
     * @param path1 path to the reference file (file 1 — IDs collected from here)
     * @param path2 path to the file to check against file 1
     * @return true if the files are structurally equivalent under the ID-mapping
     *         rules
     */
    public boolean compare(String path1, String path2) throws IOException {
        String json1 = new String(Files.readAllBytes(Paths.get(path1)));
        String json2 = new String(Files.readAllBytes(Paths.get(path2)));
        Node tree1 = new JsonParser(json1).parse();
        Node tree2 = new JsonParser(json2).parse();
        collectIds(tree1);
        return compareNodes(tree1, tree2, null);
    }

    // =========================================================================
    // ID collection — walks tree1, records values of every "id" key
    // =========================================================================

    /**
     * Recursively walks the given node and collects all string values associated with the "id" key
     * into the {@code knownIds} set.
     *
     * @param node the node to start collecting IDs from
     */
    private void collectIds(Node node) {
        if (node instanceof JObj) {
            JObj obj = (JObj) node;
            for (String key : obj.keyOrder) {
                Node val = obj.fields.get(key);
                if ("id".equals(key) && val instanceof JStr) {
                    knownIds.add(((JStr) val).v);
                }
                collectIds(val);
            }
        } else if (node instanceof JArr) {
            for (Node e : ((JArr) node).elems)
                collectIds(e);
        }
    }

    // =========================================================================
    // Core comparison
    // =========================================================================

    /**
     * Compares two nodes.
     *
     * @param n1  the first node to compare
     * @param n2  the second node to compare
     * @param key the JSON key under which these nodes appear (used to decide
     *            if an array is ordered or unordered); may be null
     * @return true if the nodes are equivalent, false otherwise
     */
    private boolean compareNodes(Node n1, Node n2, String key) {
        if (n1 instanceof JNull && n2 instanceof JNull)
            return true;
        if (n1 instanceof JBool && n2 instanceof JBool)
            return ((JBool) n1).v == ((JBool) n2).v;
        if (n1 instanceof JNum && n2 instanceof JNum)
            return Double.compare(((JNum) n1).v, ((JNum) n2).v) == 0;
        if (n1 instanceof JStr && n2 instanceof JStr)
            return compareStrings(((JStr) n1).v, ((JStr) n2).v);
        if (n1 instanceof JObj && n2 instanceof JObj)
            return compareObjects((JObj) n1, (JObj) n2, key);
        if (n1 instanceof JArr && n2 instanceof JArr)
            return compareArrays((JArr) n1, (JArr) n2, key);
        return false; // type mismatch
    }

    // --- string comparison with ID mapping ---

    /**
     * Compares two strings. If the first string is a known ID, it uses the ID mapping rules.
     * Otherwise, it performs an exact string comparison.
     *
     * @param s1 the first string
     * @param s2 the second string
     * @return true if the strings match under the comparison rules
     */
    private boolean compareStrings(String s1, String s2) {
        if (!knownIds.contains(s1)) {
            // Plain string: exact match
            return s1.equals(s2);
        }
        // s1 is a known ID
        if (fwdMap.containsKey(s1)) {
            return fwdMap.get(s1).equals(s2);
        }
        // New mapping: ensure s2 is not already claimed
        if (revMap.containsKey(s2)) {
            return false; // s2 is already mapped from a different file-1 ID
        }
        fwdMap.put(s1, s2);
        revMap.put(s2, s1);
        return true;
    }

    // --- object comparison ---

    /**
     * Compares two JObj nodes. Handles both normal and ID-keyed objects.
     *
     * @param o1        the first object node
     * @param o2        the second object node
     * @param parentKey the key under which this object appears in its parent
     * @return true if the objects are equivalent
     */
    private boolean compareObjects(JObj o1, JObj o2, String parentKey) {
        if (o1.keyOrder.size() != o2.keyOrder.size())
            return false;

        // Special case: objects whose keys are IDs (e.g. bank.accounts, lane.vehicles).
        // Detected when every key in o1 is a known ID, or explicitly by field name.
        boolean isIdKeyed = !o1.keyOrder.isEmpty() &&
                (o1.keyOrder.stream().allMatch(knownIds::contains) || "accounts".equals(parentKey));

        if (isIdKeyed) {
            return compareIdKeyedObjects(o1, o2);
        }

        // Normal case: keys must match exactly.
        if (!o1.fields.keySet().equals(o2.fields.keySet()))
            return false;
        for (String key : o1.keyOrder) {
            if (!compareNodes(o1.fields.get(key), o2.fields.get(key), key))
                return false;
        }
        return true;
    }

    /**
     * Compares two objects whose keys are IDs (like bank.accounts {"player_1": 100}
     * or lane.vehicles [{"veh_car_1": 2}]).
     * Uses backtracking to find a consistent pairing of key-value entries.
     *
     * @param o1 the first ID-keyed object
     * @param o2 the second ID-keyed object
     * @return true if a consistent mapping exists
     */
    private boolean compareIdKeyedObjects(JObj o1, JObj o2) {
        List<String> keys1 = o1.keyOrder;
        List<String> keys2 = new ArrayList<>(o2.keyOrder);
        boolean[] used = new boolean[keys2.size()];
        return matchIdKeyedEntries(o1, o2, keys1, keys2, 0, used);
    }

    /**
     * Helper for {@link #compareIdKeyedObjects(JObj, JObj)} that uses backtracking to match
     * entries of ID-keyed objects.
     *
     * @param o1    the first object
     * @param o2    the second object
     * @param keys1 the keys of the first object
     * @param keys2 the keys of the second object
     * @param idx   the current index in keys1 being matched
     * @param used  tracks which keys in keys2 have already been paired
     * @return true if a valid matching is found from the current state
     */
    private boolean matchIdKeyedEntries(JObj o1, JObj o2,
            List<String> keys1, List<String> keys2, int idx, boolean[] used) {
        if (idx == keys1.size())
            return true;
        String k1 = keys1.get(idx);

        // Optimization/Robustness: Try matching with the identical key first if it
        // exists and is unused
        int identicalIdx = keys2.indexOf(k1);
        if (identicalIdx != -1 && !used[identicalIdx]) {
            Map<String, String> sf = snapshot(fwdMap);
            Map<String, String> sr = snapshot(revMap);
            if (compareStrings(k1, k1)
                    && compareNodes(o1.fields.get(k1), o2.fields.get(k1), k1)) {
                used[identicalIdx] = true;
                if (matchIdKeyedEntries(o1, o2, keys1, keys2, idx + 1, used))
                    return true;
                used[identicalIdx] = false;
            }
            fwdMap = sf;
            revMap = sr;
        }

        // Otherwise try all other unused keys
        for (int j = 0; j < keys2.size(); j++) {
            if (used[j] || (identicalIdx != -1 && j == identicalIdx))
                continue;
            String k2 = keys2.get(j);
            Map<String, String> sf = snapshot(fwdMap);
            Map<String, String> sr = snapshot(revMap);
            if (compareStrings(k1, k2)
                    && compareNodes(o1.fields.get(k1), o2.fields.get(k2), k1)) {
                used[j] = true;
                if (matchIdKeyedEntries(o1, o2, keys1, keys2, idx + 1, used))
                    return true;
                used[j] = false;
            }
            fwdMap = sf;
            revMap = sr;
        }
        return false;
    }

    // --- array comparison ---

    /**
     * Compares two JArr nodes.
     * "recoveryQueue" is the only strictly ordered array. Everything else is
     * unordered.
     *
     * @param a1  the first array node
     * @param a2  the second array node
     * @param key the key under which this array appears
     * @return true if the arrays are equivalent
     */
    private boolean compareArrays(JArr a1, JArr a2, String key) {
        if (a1.elems.size() != a2.elems.size())
            return false;
        if ("recoveryQueue".equals(key)) {
            for (int i = 0; i < a1.elems.size(); i++) {
                if (!compareNodes(a1.elems.get(i), a2.elems.get(i), null))
                    return false;
            }
            return true;
        }
        // Unordered: backtracking match
        return matchUnordered(a1.elems, a2.elems, 0, new boolean[a2.elems.size()]);
    }

    /**
     * Uses backtracking to match elements of two unordered arrays.
     *
     * @param l1   the first list of nodes
     * @param l2   the second list of nodes
     * @param idx  the current index in l1 being matched
     * @param used tracks which nodes in l2 have already been paired
     * @return true if a valid matching is found
     */
    private boolean matchUnordered(List<Node> l1, List<Node> l2, int idx, boolean[] used) {
        if (idx == l1.size())
            return true;
        Node n1 = l1.get(idx);
        for (int j = 0; j < l2.size(); j++) {
            if (used[j])
                continue;
            Map<String, String> sf = snapshot(fwdMap);
            Map<String, String> sr = snapshot(revMap);
            if (compareNodes(n1, l2.get(j), null)) {
                used[j] = true;
                if (matchUnordered(l1, l2, idx + 1, used))
                    return true;
                used[j] = false;
            }
            fwdMap = sf;
            revMap = sr;
        }
        return false;
    }

    // =========================================================================
    // Helpers
    // =========================================================================

    /**
     * Creates a shallow copy of the given map.
     *
     * @param m the map to snapshot
     * @return a new map containing the same entries
     */
    private static Map<String, String> snapshot(Map<String, String> m) {
        return new HashMap<>(m);
    }

    // =========================================================================
    // Minimal JSON parser
    // =========================================================================

    /**
     * A simple, recursive-descent JSON parser that builds a tree of {@link Node}s.
     */
    private static class JsonParser {
        /** The JSON string being parsed. */
        private final String s;
        /** The current position in the string. */
        private int pos;

        /**
         * Constructs a JsonParser.
         *
         * @param s the JSON string to parse
         */
        JsonParser(String s) {
            this.s = s;
        }

        /**
         * Parses the entire JSON string.
         *
         * @return the root node of the parsed JSON tree
         */
        Node parse() {
            skipWs();
            Node n = parseValue();
            skipWs();
            return n;
        }

        /**
         * Parses a single JSON value.
         *
         * @return the node representing the parsed value
         */
        private Node parseValue() {
            char c = s.charAt(pos);
            if (c == '{')
                return parseObject();
            if (c == '[')
                return parseArray();
            if (c == '"')
                return parseString();
            if (c == 't' || c == 'f')
                return parseBool();
            if (c == 'n')
                return parseNull();
            return parseNumber();
        }

        /**
         * Parses a JSON object.
         *
         * @return the {@link JObj} node
         */
        private JObj parseObject() {
            consume('{');
            List<String> keyOrder = new ArrayList<>();
            Map<String, Node> fields = new LinkedHashMap<>();
            skipWs();
            if (peek() == '}') {
                pos++;
                return new JObj(keyOrder, fields);
            }
            while (true) {
                skipWs();
                String key = ((JStr) parseString()).v;
                skipWs();
                consume(':');
                skipWs();
                Node val = parseValue();
                keyOrder.add(key);
                fields.put(key, val);
                skipWs();
                if (peek() == ',') {
                    pos++;
                } else
                    break;
            }
            skipWs();
            consume('}');
            return new JObj(keyOrder, fields);
        }

        /**
         * Parses a JSON array.
         *
         * @return the {@link JArr} node
         */
        private JArr parseArray() {
            consume('[');
            List<Node> elems = new ArrayList<>();
            skipWs();
            if (peek() == ']') {
                pos++;
                return new JArr(elems);
            }
            while (true) {
                skipWs();
                elems.add(parseValue());
                skipWs();
                if (peek() == ',') {
                    pos++;
                } else
                    break;
            }
            skipWs();
            consume(']');
            return new JArr(elems);
        }

        /**
         * Parses a JSON string, including escaped characters.
         *
         * @return the {@link JStr} node
         */
        private JStr parseString() {
            consume('"');
            StringBuilder sb = new StringBuilder();
            while (pos < s.length()) {
                char c = s.charAt(pos++);
                if (c == '"')
                    break;
                if (c == '\\') {
                    char e = s.charAt(pos++);
                    switch (e) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        default:
                            sb.append(e);
                    }
                } else {
                    sb.append(c);
                }
            }
            return new JStr(sb.toString());
        }

        /**
         * Parses a JSON boolean (true/false).
         *
         * @return the {@link JBool} node
         */
        private JBool parseBool() {
            if (s.startsWith("true", pos)) {
                pos += 4;
                return new JBool(true);
            }
            pos += 5;
            return new JBool(false);
        }

        /**
         * Parses a JSON null value.
         *
         * @return the singleton {@link JNull} instance
         */
        private JNull parseNull() {
            pos += 4;
            return JNull.I;
        }

        /**
         * Parses a JSON number, supporting decimal and exponential notation.
         *
         * @return the {@link JNum} node
         */
        private JNum parseNumber() {
            int start = pos;
            if (peek() == '-')
                pos++;
            while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                pos++;
            if (pos < s.length() && s.charAt(pos) == '.') {
                pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                    pos++;
            }
            if (pos < s.length() && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
                pos++;
                if (pos < s.length() && (s.charAt(pos) == '+' || s.charAt(pos) == '-'))
                    pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                    pos++;
            }
            return new JNum(Double.parseDouble(s.substring(start, pos)));
        }

        /**
         * Skips whitespace characters at the current position.
         */
        private void skipWs() {
            while (pos < s.length() && s.charAt(pos) <= ' ')
                pos++;
        }

        /**
         * Returns the character at the current position without advancing.
         *
         * @return the character at {@code pos}, or 0 if EOF
         */
        private char peek() {
            return pos < s.length() ? s.charAt(pos) : 0;
        }

        /**
         * Asserts that the current character matches {@code c} and advances.
         *
         * @param c the character to expect
         * @throws RuntimeException if the current character does not match
         */
        private void consume(char c) {
            if (pos >= s.length() || s.charAt(pos) != c)
                throw new RuntimeException(
                        "Expected '" + c + "' at pos " + pos +
                                " but got '" + (pos < s.length() ? s.charAt(pos) : "EOF") + "'");
            pos++;
        }
    }

    // =========================================================================
    // Entry point
    // =========================================================================

    /**
     * Command-line entry point.
     * Usage: java StateComparator <reference.json> <candidate.json>
     * Prints "MATCH" or "MISMATCH".
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: StateComparator <reference.json> <candidate.json>");
            return;
        }
        boolean result = new StateComparator().compare(args[0], args[1]);
        System.out.println(result ? "MATCH" : "MISMATCH");
    }
}
