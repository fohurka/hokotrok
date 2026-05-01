/**
 * Parses raw text commands from standard input and dispatches them
 * to the appropriate GameController or TestManager method.
 *
 * System commands are prefixed with '/':
 * /save, /load, /rand, /teststart, /testend,
 * /init, /roadnetwork, /vehicles, /tick, /new
 *
 * Player commands have no prefix:
 * change, status, move, purchase, equip
 *
 * The /new command is further dispatched to sub-commands via parseNew().
 */
public class CommandParser {
    private final GameController controller;
    private final TestManager testManager;

    /**
     * Constructs a CommandParser wired to the given controller and test manager.
     *
     * @param controller  the game controller that handles all game commands
     * @param testManager the test manager that handles test start/end
     */
    public CommandParser(GameController controller, TestManager testManager) {
        this.controller = controller;
        this.testManager = testManager;
    }

    /**
     * Parses and dispatches a single line of input.
     * The line is split on whitespace; the first token is the command name
     * and the remaining tokens are passed as arguments to the handler.
     *
     * @param line the raw input line; leading/trailing whitespace is ignored
     */
    public void handle(String line) {
        String[] tokens = line.trim().split("\\s+");
        String cmd = tokens[0];
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, args.length);

        switch (cmd) {
            case "/save":
                controller.save(args);
                break;
            case "/load":
                controller.load(args);
                break;
            case "/rand":
                controller.setRand(args);
                break;
            case "/teststart":
                testManager.start(args);
                break;
            case "/testend":
                testManager.end();
                break;
            case "/init":
                controller.init();
                break;
            case "/roadnetwork":
                controller.printRoadNetwork();
                break;
            case "/vehicles":
                controller.printVehicles();
                break;
            case "/tick":
                controller.tick();
                break;
            case "/new":
                parseNew(args);
                break;
            case "change":
                controller.change(args);
                break;
            case "status":
                controller.status();
                break;
            case "move":
                controller.move(args);
                break;
            case "purchase":
                controller.purchase(args);
                break;
            case "equip":
                controller.equip(args);
                break;
            case "progress":
                controller.progress(args);
                break;
            default:
                System.out.println("Unknown command: " + cmd);
        }
    }

    /**
     * Dispatches /new sub-commands that create new game objects.
     *
     * Expected format: /new <subcommand> [args...]
     * Supported sub-commands:
     * junction - creates a new Junction
     * lane [args] - creates a new Lane
     * building [args] - creates a new Building
     * carplayer [args] - creates a new CarPlayer
     * snowplowplayer [args] - creates a new SnowplowPlayer
     * busplayer [args] - creates a new BusPlayer
     *
     * @param args the arguments following /new; args[0] is the sub-command name
     */
    private void parseNew(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: /new <junction|lane|building|carplayer|snowplowplayer|busplayer>");
            return;
        }

        String[] rest = new String[args.length - 1];
        System.arraycopy(args, 1, rest, 0, rest.length);

        switch (args[0]) {
            case "junction":
                controller.newJunction();
                break;
            case "lane":
                controller.newLane(rest);
                break;
            case "building":
                controller.newBuilding(rest);
                break;
            case "carplayer":
                controller.newCarPlayer(rest);
                break;
            case "snowplowplayer":
                controller.newSnowplowPlayer(rest);
                break;
            case "busplayer":
                controller.newBusPlayer(rest);
                break;
            case "surface":
                controller.newSurface(rest);
                break;
            default:
                System.out.println("Unknown /new subcommand: " + args[0]);
        }
    }
}
