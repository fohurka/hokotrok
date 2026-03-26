public class Skeleton {
    private int indentLevel;

    /**
     * Initializes a new Skeleton instance with an indentation level of 0.
     */
    public Skeleton() {
        indentLevel = 0;
    }

    /**
     * Generates a string of tab characters corresponding to the current indentation
     * level.
     *
     * @return A string containing the appropriate number of tabs.
     */
    private String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentLevel; i++) {
            sb.append("\t");
        }
        return sb.toString();
    }

    /**
     * Prints a formatted function call message with the current indentation,
     * then increments the indentation level for subsequent calls.
     *
     * @param functionName The name of the function being called.
     */
    public void printFunctionCall(String functionName) {
        System.out.println(getIndent() + functionName);
        indentLevel++;
    }

    /**
     * Decrements the indentation level and prints a formatted return message
     * matching the previous function call's indentation.
     */
    public void printReturn() {
        indentLevel--;
        System.out.println(getIndent() + "return");
    }

    public boolean askBool(String question) {
        return true; // TODO
    }
}
