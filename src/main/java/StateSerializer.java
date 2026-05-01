import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts the live domain object graph into a JSON string.
 *
 * Usage:
 *   String json = new StateSerializer().serialize(controller);
 *
 * Approach:
 *   1. Walk the object graph section by section.
 *   2. Emit JSON directly via the inner JsonWriter helper.
 */
public class StateSerializer {

    // =========================================================================
    // Public API
    // =========================================================================

    /**
     * Serializes the complete game state held by the controller to a JSON string.
     *
     * @param controller the live GameController containing all game objects
     * @return a JSON string representing the current game state
     */
    public String serialize(GameController controller) {
        JsonWriter w = new JsonWriter();
        w.beginObject();
        w.name("gameState");
        w.beginObject();

        writeBank(w, controller);
        writeWarehouse(w, controller);
        writeRecoverer(w, controller);
        writeMap(w, controller);
        writePlayers(w, controller);
        writeVehicles(w, controller);
        writeEquipments(w, controller);

        w.endObject(); // gameState
        w.endObject(); // root
        return w.toString();
    }

    // =========================================================================
    // Section writers
    // =========================================================================

    /**
     * Writes the "bank" section: an object whose "accounts" key maps
     * each player's id to their current balance.
     * Skipped entirely if the controller has no bank.
     */
    private void writeBank(JsonWriter w, GameController ctrl) {
        Bank bank = ctrl.getBank();
        if (bank == null)
            return;
        w.name("bank");
        w.beginObject();
        w.name("accounts");
        w.beginObject();
        for (Map.Entry<Player, Integer> entry : bank.getAccounts().entrySet()) {
            w.name(entry.getKey().getId());
            w.value(entry.getValue());
        }
        w.endObject();
        w.endObject();
    }

    /**
     * Writes the "warehouse" section: id plus the list of equipment IDs
     * currently in stock.
     * Skipped entirely if the controller has no warehouse.
     */
    private void writeWarehouse(JsonWriter w, GameController ctrl) {
        Warehouse wh = ctrl.getWarehouse();
        if (wh == null)
            return;
        w.name("warehouse");
        w.beginObject();
        w.name("id");
        w.value(wh.getId());
        w.name("stock");
        w.beginArray();
        for (Equipment eq : wh.getStock()) {
            w.value(eq.getId());
        }
        w.endArray();
        w.endObject();
    }

    /**
     * Writes the "recoverer" section: id plus the ordered list of crashed
     * car IDs waiting for recovery.
     * Skipped entirely if the controller has no recoverer.
     */
    private void writeRecoverer(JsonWriter w, GameController ctrl) {
        Recoverer rec = ctrl.getRecoverer();
        if (rec == null)
            return;
        w.name("recoverer");
        w.beginObject();
        w.name("id");
        w.value(rec.getId());
        w.name("recoveryQueue");
        w.beginArray();
        for (Car c : rec.getRecoveryQueue()) {
            w.value(c.getId());
        }
        w.endArray();
        w.endObject();
    }

    /**
     * Writes the "map" section containing three sub-arrays:
     * "junctions", "buildings", and "lanes".
     */
    private void writeMap(JsonWriter w, GameController ctrl) {
        w.name("map");
        w.beginObject();

        // --- junctions ---
        w.name("junctions");
        w.beginArray();
        for (Junction j : ctrl.getJunctions()) {
            w.beginObject();
            w.name("id");
            w.value(j.getId());

            w.name("startingLaneIds");
            w.beginArray();
            for (Lane l : j.getStartingLanes())
                w.value(l.getId());
            w.endArray();

            w.name("endingLaneIds");
            w.beginArray();
            for (Lane l : j.getEndingLanes())
                w.value(l.getId());
            w.endArray();

            w.name("buildingIds");
            w.beginArray();
            for (Building b : j.getBuildings())
                w.value(b.getId());
            w.endArray();

            w.endObject();
        }
        w.endArray();

        // --- buildings ---
        w.name("buildings");
        w.beginArray();
        for (Building b : ctrl.getBuildings()) {
            w.beginObject();
            w.name("id");
            w.value(b.getId());
            w.name("connectionId");
            w.value(b.getConnection().getId());
            w.name("type");
            w.value(b instanceof Warehouse ? "Warehouse" : "Normal");
            w.endObject();
        }
        w.endArray();

        // --- lanes ---
        w.name("lanes");
        w.beginArray();
        for (Lane l : ctrl.getLanes()) {
            writeLane(w, l, ctrl);
        }
        w.endArray();

        w.endObject(); // map
    }

    /**
     * Writes a single lane object including its surface and the progress
     * positions of all vehicles currently on the lane.
     *
     * @param l    the lane to serialise
     * @param ctrl the controller (currently unused but kept for consistency)
     */
    private void writeLane(JsonWriter w, Lane l, GameController ctrl) {
        w.beginObject();
        w.name("id");
        w.value(l.getId());
        w.name("length");
        w.value(l.getLength());
        w.name("startJunctionId");
        w.value(l.getStart().getId());
        w.name("endJunctionId");
        w.value(l.getEnd().getId());

        Lane left = l.getLeftNeighbor();
        Lane right = l.getRightNeighbor();
        w.name("leftNeighborId");
        if (left == null)
            w.valueNull();
        else
            w.value(left.getId());
        w.name("rightNeighborId");
        if (right == null)
            w.valueNull();
        else
            w.value(right.getId());

        // vehicles with their progress positions
        w.name("vehicles");
        w.beginArray();
        for (Map.Entry<Vehicle, Integer> entry : l.getProgress().entrySet()) {
            w.beginObject();
            w.name(entry.getKey().getId());
            w.value(entry.getValue());
            w.endObject();
        }
        w.endArray();

        w.name("isCrashed");
        w.value(l.isCrashed());

        // surface
        writeSurface(w, l.getSurface());

        w.endObject();
    }

    /**
     * Writes the "surface" key with the surface's type discriminator,
     * snow/ice amounts, thresholds, and active modifier.
     * Writes JSON null if the surface is null.
     */
    private void writeSurface(JsonWriter w, Surface s) {
        w.name("surface");
        if (s == null) {
            w.valueNull();
            return;
        }
        w.beginObject();
        w.name("type");
        w.value(surfaceTypeName(s));
        w.name("snowAmount");
        w.value(s.getSnowAmount());
        w.name("iceAmount");
        w.value(s.getIceAmount());
        w.name("snowThreshold");
        w.value(Surface.snowThreshold);
        w.name("iceThreshold");
        w.value(Surface.iceThreshold);
        w.name("modifier");
        w.value(modifierName(s.getModifier()));
        w.endObject();
    }

    /**
     * Writes the "players" array in order: all CarPlayers, then BusPlayers,
     * then SnowplowPlayers. Each entry includes a "type" discriminator field.
     */
    private void writePlayers(JsonWriter w, GameController ctrl) {
        w.name("players");
        w.beginArray();

        for (CarPlayer p : ctrl.getCarPlayers()) {
            w.beginObject();
            w.name("id");
            w.value(p.getId());
            w.name("type");
            w.value("CarPlayer");
            w.name("vehicleId");
            w.value(p.getCar().getId());
            w.name("homeId");
            w.value(p.getHome().getId());
            w.name("workId");
            w.value(p.getWork().getId());
            w.endObject();
        }

        for (BusPlayer p : ctrl.getBusPlayers()) {
            w.beginObject();
            w.name("id");
            w.value(p.getId());
            w.name("type");
            w.value("BusPlayer");
            w.name("vehicleId");
            w.value(p.getBus().getId());
            List<Building> stations = p.getBus().getStations();
            w.name("stop1");
            if (stations == null || stations.size() < 1 || stations.get(0) == null)
                w.valueNull();
            else
                w.value(stations.get(0).getId());
            w.name("stop2");
            if (stations == null || stations.size() < 2 || stations.get(1) == null)
                w.valueNull();
            else
                w.value(stations.get(1).getId());
            w.endObject();
        }

        for (SnowplowPlayer p : ctrl.getSnowplowPlayers()) {
            w.beginObject();
            w.name("id");
            w.value(p.getId());
            w.name("type");
            w.value("SnowplowPlayer");
            w.name("vehicleIds");
            w.beginArray();
            for (Snowplow sp : p.getSnowplows())
                w.value(sp.getId());
            w.endArray();
            w.name("warehouseId");
            if (p.getWarehouse() == null)
                w.valueNull();
            else
                w.value(p.getWarehouse().getId());
            w.endObject();
        }

        w.endArray();
    }

    /**
     * Writes the "vehicles" array in order: Cars (via CarPlayers),
     * Buses (via BusPlayers), and Snowplows (via SnowplowPlayers).
     * For Cars, isCrashed is derived from the recoverer's queue rather
     * than from a dedicated field on Car.
     */
    private void writeVehicles(JsonWriter w, GameController ctrl) {
        Recoverer rec = ctrl.getRecoverer();
        w.name("vehicles");
        w.beginArray();

        for (CarPlayer cp : ctrl.getCarPlayers()) {
            Car c = cp.getCar();
            w.beginObject();
            w.name("id");
            w.value(c.getId());
            w.name("type");
            w.value("Car");
            w.name("ownerId");
            w.value(cp.getId());
            writeLocation(w, c.getLocation());
            boolean crashed = rec != null && rec.getRecoveryQueue().contains(c);
            w.name("isCrashed");
            w.value(crashed);
            w.endObject();
        }

        for (BusPlayer bp : ctrl.getBusPlayers()) {
            Bus b = bp.getBus();
            w.beginObject();
            w.name("id");
            w.value(b.getId());
            w.name("type");
            w.value("Bus");
            w.name("ownerId");
            w.value(bp.getId());
            writeLocation(w, b.getLocation());
            w.name("isCrashed");
            w.value(b.isCrashed());
            w.endObject();
        }

        for (SnowplowPlayer sp : ctrl.getSnowplowPlayers()) {
            for (Snowplow plow : sp.getSnowplows()) {
                w.beginObject();
                w.name("id");
                w.value(plow.getId());
                w.name("type");
                w.value("Snowplow");
                w.name("ownerId");
                w.value(sp.getId());
                writeLocation(w, plow.getLocation());
                Equipment eq = plow.getCurrentEquipment();
                w.name("equipmentId");
                if (eq == null)
                    w.valueNull();
                else
                    w.value(eq.getId());
                w.endObject();
            }
        }

        w.endArray();
    }

    /**
     * Writes the "equipments" array, one entry per equipment piece regardless
     * of whether it is equipped or in the warehouse.
     * Each entry includes a type discriminator and a "stateData" object.
     */
    private void writeEquipments(JsonWriter w, GameController ctrl) {
        w.name("equipments");
        w.beginArray();
        for (Equipment eq : ctrl.getAllEquipments()) {
            w.beginObject();
            w.name("id");
            w.value(eq.getId());
            w.name("type");
            w.value(equipmentTypeName(eq));
            w.name("ownerId");
            if (eq.owner == null)
                w.valueNull();
            else
                w.value(eq.owner.getId());
            w.name("stateData");
            writeEquipmentStateData(w, eq);
            w.endObject();
        }
        w.endArray();
    }

    // =========================================================================
    // Helper writers
    // =========================================================================

    /**
     * Writes the "location" key with a sub-object containing "type"
     * ("Lane" or "Junction") and "id".
     * Writes JSON null if loc is null.
     */
    private void writeLocation(JsonWriter w, MapComponent loc) {
        w.name("location");
        if (loc == null) {
            w.valueNull();
            return;
        }
        w.beginObject();
        if (loc instanceof Lane) {
            w.name("type");
            w.value("Lane");
        } else if (loc instanceof Junction) {
            w.name("type");
            w.value("Junction");
        } else {
            w.name("type");
            w.value("Unknown");
        }
        w.name("id");
        w.value(loc.getId());
        w.endObject();
    }

    /**
     * Writes the "stateData" object for an equipment piece.
     * Salter, DragonBlade, and Gritter emit {"ammo": <int>}.
     * All other types emit an empty object {}.
     */
    private void writeEquipmentStateData(JsonWriter w, Equipment eq) {
        w.beginObject();
        if (eq instanceof Salter) {
            w.name("ammo");
            w.value(((Salter) eq).getAmmo());
        } else if (eq instanceof DragonBlade) {
            w.name("ammo");
            w.value(((DragonBlade) eq).getAmmo());
        } else if (eq instanceof Gritter) {
            w.name("ammo");
            w.value(((Gritter) eq).getAmmo());
        }
        // Sweeper, Impeller, IceBreaker: no state → empty object
        w.endObject();
    }

    // =========================================================================
    // Type name helpers
    // =========================================================================

    /**
     * Returns the JSON type discriminator string for the given Surface.
     * One of: "SmallSnow", "DeepSnow", "Ice", "Grit", "Unmodified".
     */
    private String surfaceTypeName(Surface s) {
        if (s instanceof SmallSnow)
            return "SmallSnow";
        if (s instanceof DeepSnow)
            return "DeepSnow";
        if (s instanceof Ice)
            return "Ice";
        if (s instanceof Grit)
            return "Grit";
        return "Unmodified";
    }

    /**
     * Returns the JSON name of the active Modifier: "Salted" or "Unmodified".
     */
    private String modifierName(Modifier m) {
        if (m instanceof Salted)
            return "Salted";
        return "Unmodified";
    }

    /**
     * Returns the JSON type discriminator string for the given Equipment.
     * One of: "Salter", "DragonBlade", "Gritter", "Sweeper", "Impeller",
     * "IceBreaker", or "Unknown" for unrecognised types.
     */
    private String equipmentTypeName(Equipment eq) {
        if (eq instanceof Salter)
            return "Salter";
        if (eq instanceof DragonBlade)
            return "DragonBlade";
        if (eq instanceof Gritter)
            return "Gritter";
        if (eq instanceof Sweeper)
            return "Sweeper";
        if (eq instanceof Impeller)
            return "Impeller";
        if (eq instanceof IceBreaker)
            return "IceBreaker";
        return "Unknown";
    }

    // =========================================================================
    // Hand-rolled JSON writer
    // =========================================================================

    /**
     * Minimal, zero-dependency JSON writer.
     * Automatically handles comma insertion between elements.
     */
    private static class JsonWriter {
        private final StringBuilder sb = new StringBuilder();
        /**
         * Stack tracking whether the current container already wrote its first element
         */
        private final java.util.Deque<boolean[]> stack = new java.util.ArrayDeque<>();

        /** Open a JSON object '{' */
        public void beginObject() {
            writeCommaIfNeeded();
            sb.append('{');
            stack.push(new boolean[] { false });
        }

        /** Close the current JSON object '}' */
        public void endObject() {
            stack.pop();
            sb.append('}');
            markWritten();
        }

        /** Open a JSON array '[' */
        public void beginArray() {
            writeCommaIfNeeded();
            sb.append('[');
            stack.push(new boolean[] { false });
        }

        /** Close the current JSON array ']' */
        public void endArray() {
            stack.pop();
            sb.append(']');
            markWritten();
        }

        /** Write an object key (must be followed immediately by a value call) */
        public void name(String key) {
            writeCommaIfNeeded();
            appendString(key);
            sb.append(':');
            // The name-colon doesn't count as "written" — the value does
            // So we undo the mark that writeCommaIfNeeded caused:
            if (!stack.isEmpty())
                stack.peek()[0] = false;
        }

        /** Write a String value, or JSON null if the value is null. */
        public void value(String v) {
            writeCommaIfNeeded();
            if (v == null)
                sb.append("null");
            else
                appendString(v);
            markWritten();
        }

        /** Write an int value */
        public void value(int v) {
            writeCommaIfNeeded();
            sb.append(v);
            markWritten();
        }

        /** Write a boolean value */
        public void value(boolean v) {
            writeCommaIfNeeded();
            sb.append(v);
            markWritten();
        }

        /** Write a JSON null literal. */
        public void valueNull() {
            writeCommaIfNeeded();
            sb.append("null");
            markWritten();
        }

        @Override
        public String toString() {
            return sb.toString();
        }

        // ---- internal helpers ----

        private void writeCommaIfNeeded() {
            if (!stack.isEmpty() && stack.peek()[0]) {
                sb.append(',');
            }
        }

        private void markWritten() {
            if (!stack.isEmpty())
                stack.peek()[0] = true;
        }

        private void appendString(String s) {
            sb.append('"');
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                switch (c) {
                    case '"':
                        sb.append("\\\"");
                        break;
                    case '\\':
                        sb.append("\\\\");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    default:
                        sb.append(c);
                }
            }
            sb.append('"');
        }
    }
}
