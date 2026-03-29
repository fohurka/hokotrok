import java.util.Scanner;

public class Skeleton {
    private static int indentLevel = 0;
    private static final Scanner scanner = new Scanner(System.in);
    public static Boolean init;

    private Skeleton() {
    }

    /**
     * Generates a string of tab characters corresponding to the current indentation
     * level.
     *
     * @return A string containing the appropriate number of tabs.
     */
    private static String getIndent() {
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
    public static void printFunctionCall(String functionName) {
        if (!init) {
            System.out.println(getIndent() + "---->" + functionName + "()");

            indentLevel++;
        }
    }

    /**
     * Decrements the indentation level and prints a formatted return message
     * matching the previous function call's indentation.
     */
    public static void printReturn() {
        if (!init) {
            indentLevel--;

            System.out.println(getIndent() + "<----return");
        }
    }

    /**
     * Asks the user a boolean question via standard input and returns the parsed
     * answer.
     * Continuously prompts until a valid boolean-like string (true, t, igen, y, yes
     * / false, f, nem, n, no) is provided.
     *
     * @param question The question to display to the user.
     * @return True if the user inputs a positive response, false for a negative
     *         response.
     */
    public static boolean askBool(String question) {
        System.out.print(question + " (true/false):\t");
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("t") || input.equals("igen") || input.equals("y")
                    || input.equals("yes")) {
                return true;
            } else if (input.equals("false") || input.equals("f") || input.equals("nem") || input.equals("n")
                    || input.equals("no")) {
                return false;
            } else {
                System.out.print("Érvénytelen bemenet! " + question + " (true/false): ");
            }
        }
    }

    /**
     * Asks the user a question expecting an integer answer via standard input.
     * Continuously prompts until an input that can be parsed as an integer is
     * provided.
     *
     * @param question The question to display to the user.
     * @return The integer value parsed from the user's input.
     */
    public static int askInt(String question) {
        System.out.print(question);
        while (true) {
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Kérlek érvényes számot adj meg! " + question + "\t");
            }
        }
    }
}
