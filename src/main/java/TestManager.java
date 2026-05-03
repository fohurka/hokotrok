import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestManager {
    private final GameController controller;
    private String currentTestId;

    public TestManager(GameController controller) {
        this.controller = controller;
    }

    /**
     * Starts a test case: disables random events and loads the input state.
     * Usage: /teststart <testcaseID>
     *
     * @param args args[0] is the test ID (e.g. "001", "1")
     */
    public void start(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: /teststart <testcaseID>");
            return;
        }

        // Disable random events as per requirement
        controller.setRand(new String[] { "false" });

        String id = args[0];
        // Normalize ID to 3 digits for folder structure (e.g., "1" -> "001")
        if (id.length() == 1)
            id = "00" + id;
        else if (id.length() == 2)
            id = "0" + id;

        currentTestId = id;
        String inputPath = "test/" + id + "/input.json";

        try {
            if (!Files.exists(Paths.get(inputPath))) {
                System.out.println("Test input file not found: " + inputPath);
                return;
            }
            String json = new String(Files.readAllBytes(Paths.get(inputPath)));
            new StateParser().parse(json, controller);
        } catch (IOException e) {
            System.out.println("Error loading test input: " + e.getMessage());
        }
    }

    /**
     * Ends the active test case: saves the current state and compares it
     * with the expected output.
     * Usage: /testend
     */
    public void end() {
        if (currentTestId == null) {
            System.out.println("No active test case to end");
            return;
        }

        String expectedPath = "test/" + currentTestId + "/expected.json";
        String actualPath = "test/" + currentTestId + "/out.json";

        try {
            // Save the current state to a file
            String actualJson = new StateSerializer().serialize(controller);
            Files.write(Paths.get(actualPath), actualJson.getBytes());

            // Compare with expected output
            if (!Files.exists(Paths.get(expectedPath))) {
                System.out.println("Expected output file not found: " + expectedPath);
                return;
            }

            boolean match = new StateComparator().compare(expectedPath, actualPath);
            if (match) {
                System.out.println("SUCCESS");
            } else {
                System.out.println("FAIL");
            }

        } catch (IOException e) {
            System.out.println("Error during test completion: " + e.getMessage());
        } finally {
            // Delete the temporary out.json file as per requirement
            try {
                if (currentTestId != null) {
                    Files.deleteIfExists(Paths.get("test/" + currentTestId + "/out.json"));
                }
            } catch (IOException e) {
                // Ignore deletion errors
            }
            currentTestId = null; // Reset for the next test
        }
    }
}
