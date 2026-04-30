public class CommandParser {
    private final GameController controller;
    private final TestManager testManager;

    public CommandParser(GameController controller, TestManager testManager) {
        this.controller = controller;
        this.testManager = testManager;
    }

    public void handle(String line) {
        String[] tokens = line.trim().split("\\s+");
        String cmd = tokens[0];
        String[] args = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, args, 0, args.length);

        switch (cmd) {
            case "/save":         controller.save(args);           break;
            case "/load":         controller.load(args);           break;
            case "/rand":         controller.setRand(args);        break;
            case "/teststart":    testManager.start(args);         break;
            case "/testend":      testManager.end();               break;
            case "/init":         controller.init();               break;
            case "/roadnetwork":  controller.printRoadNetwork();   break;
            case "/vehicles":     controller.printVehicles();      break;
            case "/tick":         controller.tick();               break;
            case "/new":          parseNew(args);                  break;
            case "change":        controller.change(args);         break;
            case "status":        controller.status();             break;
            case "move":          controller.move(args);           break;
            case "purchase":      controller.purchase(args);       break;
            case "equip":         controller.equip(args);          break;
            default: System.out.println("Unknown command: " + cmd);
        }
    }

    private void parseNew(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: /new <junction|lane|building|carplayer|snowplowplayer|busplayer>");
            return;
        }

        String[] rest = new String[args.length - 1];
        System.arraycopy(args, 1, rest, 0, rest.length);

        switch (args[0]) {
            case "junction":       controller.newJunction();          break;
            case "lane":           controller.newLane(rest);          break;
            case "building":       controller.newBuilding(rest);      break;
            case "carplayer":      controller.newCarPlayer(rest);     break;
            case "snowplowplayer": controller.newSnowplowPlayer(rest);break;
            case "busplayer":      controller.newBusPlayer(rest);     break;
            default: System.out.println("Unknown /new subcommand: " + args[0]);
        }
    }
}
