import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean interactive = System.console() != null;
        GameController controller = new GameController();
        TestManager testManager = new TestManager(controller);
        CommandParser parser = new CommandParser(controller, testManager);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = reader.readLine()) != null) {
            int ci = line.indexOf('#');
            if (ci >= 0) line = line.substring(0, ci);
            line = line.trim();
            if (line.isEmpty()) continue;
            if (interactive) System.out.print("> ");
            parser.handle(line);
        }
    }
}
