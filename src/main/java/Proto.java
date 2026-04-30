import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Proto {

    public interface Command {
        public void execute(String[] args);
    }
    
    public class CommandException extends Exception{
        public ArgumentFormatException(String s){
            super(s);
        }
    }

    static void _main()
    {
        HashMap<String, Command> commands = new HashMap();

        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Boolean run = true;
        while (run) {
            String l = br.readLine();
            String input[] = l.split(" ");

            if (commands.containsKey(input[0]))
            {
                try {
                    commands.get(input[0]).execute(input);
                } catch (CommandException e) {
                    System.out.println(e.getMessage());
                }
            }
            else
                System.out.println("Invalid input");
        }
    }

    static boolean isRand;

    static RoadNetwork rn;
    static Bank bank;
    static Warehouse warehouse;
    static Recoverer recoverer;

    static Map<String, Junction> junctions = new LinkedHashMap<>();
    static Map<String, Lane> lanes = new LinkedHashMap<>();
    static Map<String, Building> buildings = new LinkedHashMap<>();
    static Map<String, Player> players = new LinkedHashMap<>();
    static Map<String, Vehicle> vehicles = new LinkedHashMap<>();
    static Map<String, Equipment> equipments = new LinkedHashMap<>();
    static Map<Object, String> idMap = new HashMap<>();
    
    static String activePlayerId;

    public static void save(String[] args) {
        //todo
    }

    public static void load(String[] args) {
        //todo
    }

    public static void rand(String[] args) {
        if (args.length > 1) {
            if (args[1].equals("0")) isRand = false;
            else if (args[1].equals("1")) isRand = true;
            else throw new CommandException("Invalid argument");
        } else {
           throw new CommandException("Not enough arguments");
        }
    }

    public static void teststart(String[] args) {
        //todo
    }

    public static void testend(String[] args) {
        //todo
    }

    public static void init(String[] args) {
        rn = new RoadNetwork();
        bank = new Bank();
        recoverer = new Recoverer();

        Junction wJunc = new Junction();
        String jid = "junc_1";
        junctions.put(jid, wJunc);
        idMap.put(wJunc, jid);
        rn.addJunction(wJunc);

        warehouse = new Warehouse(wJunc);
        String wid = "bldg_warehouse";
        buildings.put(wid, warehouse);
        idMap.put(warehouse, wid);
        wJunc.addBuilding(warehouse);

        rn.setBank(bank);
        bank.setWarehouse(warehouse);
        warehouse.setBank(bank);
    }

    public static void roadnetwork(String[] args) {
        printJunctions();
        for (Map.Entry<String, Lane> entry : lanes.entrySet()) {
            Lane l = entry.getValue();
            Surface s = l.getSurface();
            String type = s.getClass().getSimpleName();
            String mod = s.getModifier().getClass().getSimpleName();
            System.out.println("---Sávok: (laneID, startJunctionID, endJunctionID, length, type, modifier, snowAmount, iceAmount)---");
            System.out.printf("%s, %s, %s, %d, %s, %s, %d, %d\n",
                entry.getKey(), idMap.get(l.getStart()), idMap.get(l.getEnd()),
                l.getLength(), type, mod, s.getSnowAmount(), s.getIceAmount());
        }
        printBuildings();
    }

    public static void vehicles(String[] args) {
        List<Vehicle> snowplows = new ArrayList<>();
        List<Vehicle> buses = new ArrayList<>();
        List<Vehicle> cars = new ArrayList<>();
        for (Vehicle v : vehicles.values()) {
            if (v instanceof Snowplow) snowplows.add(v);
            else if (v instanceof Bus) buses.add(v);
            else if (v instanceof Car) cars.add(v);
        }
        System.out.println("---Hókotrók: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : snowplows) printVehicle(v);
        System.out.println("---Buszok: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : buses) printVehicle(v);
        System.out.println("---Autók: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : cars) printVehicle(v);
    }

    private static void printVehicle(Vehicle v) {
        String vid = idMap.get(v);
        String locId = idMap.get(v.getLocation());
        System.out.printf("%s, %s\n", vid, locId);
    }

    public static void newCommand(String[] args) {
        if (args.length < 2) throw new CommandException("Not enough arguments");
        switch (args[1]) {
            case "junction": newJunction(args); break;
            case "lane": newLane(args); break;
            case "building": newBuilding(args); break;
            case "carplayer": newCarplayer(args); break;
            case "snowplowplayer": newSnowplowplayer(args); break;
            case "busplayer": newBusplayer(args); break;
            case "surface": newSurface(args); break;
        }
    }

    private String validateNewID(String id) {
        if (idMap.containsValue(id)) return validateNewID(id+"_new");
        else return id;
    }

    private void printJunctions() {
        for (String jid : junctions.keySet()) {
            System.out.println("---Kereszteződések: (junctionID)---");
            System.out.println(jid);
        }
    }
    private void printBuildings() {
        for (String jid : junctions.keySet()) {
            System.out.println("---Kereszteződések: (junctionID)---");
            System.out.println(jid);
        }
    }

    public static void newJunction(String[] args) {
        if (rn == null) throw new CommandException("Game state uninitialized");
        String id = validateNewID("junc_" + (junctions.size() + 1));
        Junction j = new Junction();
        j.setRoadNetwork(rn);
        rn.addJunction(j);
        junctions.put(id, j);
        idMap.put(j, id);
    }

    public static void newLane(String[] args) {
        if (args.length < 5) throw new CommandException("Not enough arguments");
        String startId = args[2];
        String endId = args[3];
        int length;
        try {
            length = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid argument format");
        }

        Junction start = junctions.get(startId);
        Junction end = junctions.get(endId);
        if (start == null || end == null){
            printJunctions();
            return;
        }

        Lane l = new Lane(start, end, length);
        l.setRoadNetwork(rn);
        l.setSurface(new SmallSnow(l));
        rn.addLane(l);
        start.addStarting(l);
        end.addEnding(l);

        String id = validateNewID("lane_" + (lanes.size() + 1));
        lanes.put(id, l);
        idMap.put(l, id);
    }

    public static void newBuilding(String[] args) {
        if (args.length < 3) throw new CommandException("Not enough arguments");
        Junction j = junctions.get(args[2]);
        if (j == null)
        {
            printJunctions();
            return;
        }
        Building b = new Building(j);
        j.addBuilding(b);
        String id = validateNewID("bldg_" + (buildings.size() + 1));
        buildings.put(id, b);
        idMap.put(b, id);
    }

    public static void newCarplayer(String[] args) {
        if (args.length < 4) throw new CommandException("Not enough arguments");
        Building home = buildings.get(args[2]);
        Building work = buildings.get(args[3]);
        if (home == null || work == null)
        {
            printBuildings();
            return;
        }
        CarPlayer cp = new CarPlayer(home, work);
        String pid = validateNewID("player_car_" + (players.size() + 1));
        players.put(pid, cp);
        idMap.put(cp, pid);

        Car c = cp.getCar();
        c.setRecoverer(recoverer);
        c.setLocation(home.getConnection());

        String vid = validateNewID("veh_car_" + (vehicles.size() + 1));
        vehicles.put(vid, c);
        idMap.put(c, vid);
    }

    public static void newSnowplowplayer(String[] args) {
        if (args.length < 3) throw new CommandException("Not enough arguments");
        Junction j = junctions.get(args[2]);
        if (j == null)
        {
            printJunctions();
            return;
        }
        SnowplowPlayer sp = new SnowplowPlayer(j);
        sp.setWarehouse(warehouse);

        String pid = validateNewID("player_sp_" + (players.size() + 1));
        players.put(pid, sp);
        idMap.put(sp, pid);

        Snowplow s = sp.getSnowplow(0);
        String cid = validateNewID("veh_sp_" + (vehicles.size() + 1));
        vehicles.put(cid, s);
        idMap.put(s, cid);
    }

    public static void newBusplayer(String[] args) {
        if (args.length < 4) throw new CommandException("Not enough arguments");
        Building b1 = buildings.get(args[2]);
        Building b2 = buildings.get(args[3]);
        if (b1 == null || b2 == null)
        {
            printBuildings();
            return;
        }
        BusPlayer bp = new BusPlayer(b1.getConnection());
        String pid = validateNewID("player_bus_" + (players.size() + 1));
        players.put(pid, bp);
        idMap.put(bp, pid);

        Bus b = bp.getBus();
        List<Building> stations = new ArrayList<>();
        stations.add(b1);
        stations.add(b2);
        b.setStations(stations);

        String vid = validateNewID("veh_bus_" + (vehicles.size() + 1));
        vehicles.put(vid, b);
        idMap.put(b, vid);
    }

    public static void tick(String[] args) {
        rn.tick();
        recoverer.tick();
        List<Vehicle> currentVehicles = new ArrayList<>(vehicles.values());
        for (Vehicle v : currentVehicles) {
            v.tick();
        }
    }

    public static void change(String[] args) {
        if (args.length > 1) {
            if (players.containsKey(args[1]) && !players.get(args[1]) instanceof CarPlayer)
                activePlayerId = args[1];
            else{
                System.out.println("---Játékosok: (playerID)---");
                for (String k : players.keySet()) {
                    if (!players.get(k) instanceof CarPlayer) {
                        System.out.println(k);
                    }
                }
            }
        }
        else{
            throw new CommandException("Not enough arguments");
        }
    }

    public static void status(String[] args) {
        Player p = players.get(activePlayerId);
        if (p == null) throw new CommandException("No active player");
        int balance = bank.getBalance(p);
        if (p instanceof SnowplowPlayer) {
            SnowplowPlayer sp = (SnowplowPlayer) p;
            System.out.print(balance);
            for (Snowplow s : sp.getSnowplows()) {
                String vid = idMap.get(s);
                String eid = idMap.get(s.getCurrentEquipment());
                if (eid == null) eid = "null";
                System.out.print(", " + vid + ", " + eid );
            }
            System.out.println("");
        } else if (p instanceof BusPlayer) {
            BusPlayer bp = (BusPlayer) p;
            Bus b = bp.getBus();
            String vid = idMap.get(b);
            List<Building> stations = b.getStations();
            String s1 = stations.size() > 0 ? idMap.get(stations.get(0).getConnection()) : "null";
            String s2 = stations.size() > 1 ? idMap.get(stations.get(1).getConnection()) : "null";
            System.out.printf("(%s, %s, %s, %d, %b)\n", vid, s1, s2, balance, b.isCrashed());
        }
    }

    public static void move(String[] args) {
        if (activePlayerId == null) throw new CommandException("No active player");
        Player p = players.get(activePlayerId);
        if (args.length < 2) throw new CommandException("Not enough arguments");
        String destId = args[1];
        Junction dest = junctions.get(destId);
        
        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e) {
                throw new CommandException("Invalid argument format");
            }
        }
        
        if (p.getVehicles()[vIdx].getLocation() instanceof Lane) throw new CommandException("In lane");

        List<Junction> available = new ArrayList<>();
        for (Lane l : (Junction)(p.getVehicles()[vIdx].getLocation()).getLanes()) {
            available.add(l.getEnd());
        }

        if (!available.contains(dest))
        {
            System.out.println("---Elérhető kereszteződések: (junctionID)---");
            for (Junction junction : available) {
                System.out.println(idMap.get(junction));
            }
        }
        else
            p.choseDirection(dest, vIdx);
    }

    public static void purchase(String[] args) {
        Player p = players.get(activePlayerId);
        if (!(p instanceof SnowplowPlayer)) throw new CommandException("No active snowplow player");
        SnowplowPlayer sp = (SnowplowPlayer) p;
        
        if (args.length < 2) throw new CommandException("Not enough arguments");
        String item = args[1];
        if (item.equalsIgnoreCase("Snowplow")) {
            sp.buySnowplow();
            List<Snowplow> plows = sp.getSnowplows();
            if (!plows.isEmpty()) {
                Snowplow newPlow = plows.get(plows.size() - 1);
                if (!vehicles.containsValue(newPlow)) {
                    String cid = validateNewID("veh_sp_" + (vehicles.size() + 1));
                    vehicles.put(cid, newPlow);
                    idMap.put(newPlow, cid);
                }
            }
        } else {
            int id = -1;
            if (item.equalsIgnoreCase("Sweeper")) id = 1;
            else if (item.equalsIgnoreCase("Impeller")) id = 2;
            else if (item.equalsIgnoreCase("Salter")) id = 3;
            else if (item.equalsIgnoreCase("IceBreaker")) id = 4;
            else if (item.equalsIgnoreCase("DragonBlade")) id = 5;

            if (id != -1) {
                int oldSize = warehouse.getStock().size();
                sp.buyEquipment(id);
                if (warehouse.getStock().size() > oldSize) {
                    Equipment newEq = warehouse.getStock().get(warehouse.getStock().size() - 1);
                    String eid = validateNewID("eq_" + item.toLowerCase() + "_" + (equipments.size() + 1));
                    equipments.put(eid, newEq);
                    idMap.put(newEq, eid);
                    //todo ha nm sikerül venni
                }
            } else {
                System.out.println("Invalid item");
            }
        }
    }

    public static void equip(String[] args) {
        Player p = players.get(activePlayerId);
        if (!(p instanceof SnowplowPlayer)) throw new CommandException("No active snowplow player");
        SnowplowPlayer sp = (SnowplowPlayer) p;

        if (args.length < 2) throw new CommandException("Not enough arguments");
        String eqName = args[1];
        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e) {
                throw new CommandException("Invalid argument format");
            }
        }
        Snowplow plow = sp.getSnowplow(vIdx);

        if(plow.getLocation() instanceof Lane || !(Junction)(plow.getLocation()).equals(warehouse.getConnection())){
             System.out.println("A kotró nincs a raktárban");
        }
        
        Equipment toEquip = null;
        for (Equipment eq : warehouse.getStock()) {
            if (eq.getClass().getSimpleName().equalsIgnoreCase(eqName)) {
                toEquip = eq;
                break;
            }
        }
        if (toEquip != null) {
            warehouse.changeEquipment(plow, toEquip);
        } else {
            System.out.println("Equipment not available");
        }
    }

    public static void newSurface(String[] args) {
        if (args.length < 7) throw new CommandException("Not enough arguments");
        Lane l = lanes.get(args[2]);
        if (l == null) {
            printJunctions();
            return;
        }
        String type = args[3];
        String modStr = args[4];
        int snow;
        int ice;
        try {
            snow = Integer.parseInt(args[5]);
            ice = Integer.parseInt(args[6]);
        }
        catch (NumberFormatException e) {
            throw new CommandException("Invalid argument format");
        }

        Modifier mod = modStr.equals("Salted") ? new Salted() : new Unmodified();
        Surface surf;
        switch (type) {
            case "SmallSnow": surf = new SmallSnow(l, mod); break;
            case "DeepSnow": surf = new DeepSnow(l, mod); break;
            case "Ice": surf = new Ice(l, mod); break;
            case "Grit": surf = new Grit(l, mod); break;
            default: surf = new SmallSnow(l, mod); break;
        }
        surf.addSnow(snow);
        surf.addIce(ice);
        l.setSurface(surf);
    }

    public static void progress(String[] args) {
        if (activePlayerId == null) throw new CommandException("No active player");
        Player p = players.get(activePlayerId);
        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            }
            catch (NumberFormatException e) {
                throw new CommandException("Invalid argument format");
            }
        }
        Vehicle v = p.getVehicles()[vIdx];
        v.getLocation().progress(v);
    }
}
