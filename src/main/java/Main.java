import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The entry point of the Hokotrok application.
 * It initializes the game components and starts the command processing loop.
 */
public class Main {
    /**
     * The main method that reads commands from the standard input and dispatches them to the parser.
     * Supports both interactive and non-interactive modes.
     * 
     * @param args Command-line arguments.
     * @throws Exception If an error occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        boolean interactive = System.console() != null;
        GameController controller = new GameController();
        TestManager testManager = new TestManager(controller);
        CommandParser parser = new CommandParser(controller, testManager);

        if (interactive) {
            System.out.println("Hokotrok started");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (interactive)
            System.out.print("> ");
        String line;
        while ((line = reader.readLine()) != null) {
            int ci = line.indexOf('#');
            if (ci >= 0)
                line = line.substring(0, ci);
            line = line.trim();
            if (line.isEmpty()) {
                if (interactive)
                    System.out.print("> ");
                continue;
            }
            parser.handle(line);
            if (interactive)
                System.out.print("> ");
        }
    }
}
