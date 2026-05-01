import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Central game controller that owns and manages all live domain objects.
 *
 * This class is the single source of truth for the current game state.
 * It is the primary target of CommandParser dispatches, the object graph
 * exposed to StateSerializer for save operations, and the object filled
 * by StateParser during load operations.
 *
 * Each entity type (junctions, lanes, buildings, players, vehicles,
 * equipments) is kept in a dedicated list. The singleton objects
 * (Bank, Warehouse, Recoverer) are held as plain fields.
 *
 * Game command methods are currently stubs to be filled in later.
 */
public class GameController {

    // -------------------------------------------------------------------------
    // Game object collections — populated by /new commands or /load
    // -------------------------------------------------------------------------

    private Bank bank;
    private Warehouse warehouse;
    private Recoverer recoverer;
    private RoadNetwork rn;
    private String activePlayerId;

    private final Map<String, Junction> junctions = new LinkedHashMap<>();
    private final Map<String, Building> buildings = new LinkedHashMap<>();
    private final Map<String, Lane> lanes = new LinkedHashMap<>();
    private final Map<String, CarPlayer> carPlayers = new LinkedHashMap<>();
    private final Map<String, BusPlayer> busPlayers = new LinkedHashMap<>();
    private final Map<String, SnowplowPlayer> snowplowPlayers = new LinkedHashMap<>();
    private final Map<String, Equipment> equipments = new LinkedHashMap<>();
    private final Map<String, Vehicle> vehicles = new LinkedHashMap<>();
    private final Map<Object, String> idMap = new HashMap<>();

    // -------------------------------------------------------------------------
    // Helper ID lookups
    // -------------------------------------------------------------------------

    private String validateNewId(String id) {
        if (idMap.containsValue(id))
            return validateNewId(id + "_new");
        return id;
    }

    // -------------------------------------------------------------------------
    // Lifecycle commands
    // -------------------------------------------------------------------------

    /** Initialises the game to a clean default state. */
    public void init() {
        rn = new RoadNetwork();
        bank = new Bank();
        recoverer = new Recoverer();

        Junction wJunc = new Junction();
        String jid = "junc_1";
        wJunc.setId(jid);
        junctions.put(jid, wJunc);
        idMap.put(wJunc, jid);
        rn.addJunction(wJunc);

        warehouse = new Warehouse(wJunc);
        String wid = "bldg_warehouse";
        warehouse.setId(wid);
        buildings.put(wid, warehouse);
        idMap.put(warehouse, wid);
        wJunc.addBuilding(warehouse);

        rn.setBank(bank);
        bank.setWarehouse(warehouse);
        warehouse.setBank(bank);
    }

    // -------------------------------------------------------------------------
    // Save / Load
    // -------------------------------------------------------------------------

    /**
     * Serializes the current game state to a JSON file.
     * Usage: /save <filename>
     *
     * @param args args[0] = output filename
     */
    public void save(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: /save <filename>");
            return;
        }
        String filename = args[0];
        try {
            String json = new StateSerializer().serialize(this);
            Files.write(Paths.get(filename), json.getBytes());
            System.out.println("State saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    /**
     * Loads a game state from a JSON file, replacing the current state.
     * Usage: /load <filename>
     *
     * @param args args[0] = input filename
     */
    public void load(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: /load <filename>");
            return;
        }
        String filename = args[0];
        try {
            String json = new String(Files.readAllBytes(Paths.get(filename)));
            new StateParser().parse(json, this);
            System.out.println("State loaded from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading state: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Game commands
    // -------------------------------------------------------------------------

    /** Sets whether the events should be random or not (true/false) */
    public void setRand(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: /setRand <true/false>");
            return;
        }
        boolean random = Boolean.parseBoolean(args[0]);
        // TODO
    }

    /** Advances the simulation by one tick: moves vehicles, updates surfaces. */
    public void tick() {
        if (rn != null)
            rn.tick();
        if (recoverer != null)
            recoverer.tick();
        List<Vehicle> currentVehicles = new ArrayList<>(vehicles.values());
        for (Vehicle v : currentVehicles) {
            v.tick();
        }
    }

    /** Prints a human-readable description of the road network to stdout. */
    public void printRoadNetwork() {
        printJunctions();
        printLanes();
        printBuildings();
    }

    public void printJunctions() {
        System.out.println("---Kereszteződések: (junctionID) ---");
        for (String jid : junctions.keySet()) {
            System.out.println(jid);
        }
    }

    public void printLanes() {
        System.out.println(
                "---Sávok: (laneID, startJunctionID, endJunctionID, length, type, modifier, snowAmount, iceAmount) ---");
        for (Map.Entry<String, Lane> entry : lanes.entrySet()) {
            Lane l = entry.getValue();
            Surface s = l.getSurface();
            String type = s.getClass().getSimpleName();
            String mod = s.getModifier().getClass().getSimpleName();
            System.out.printf("%s, %s, %s, %d, %s, %s, %d, %d\n",
                    entry.getKey(), l.getStart() != null ? l.getStart().getId() : "null",
                    l.getEnd() != null ? l.getEnd().getId() : "null",
                    l.getLength(), type, mod, s.getSnowAmount(), s.getIceAmount());
        }
    }

    public void printBuildings() {
        System.out.println("---Épületek: (buildingID, junctionID) ---");
        for (Map.Entry<String, Building> entry : buildings.entrySet()) {
            Building b = entry.getValue();
            System.out.printf("%s, %s\n", entry.getKey(), b.getConnection().getId());
        }
    }

    /** Prints the current state of all vehicles to stdout. */
    public void printVehicles() {
        List<Vehicle> snowplows = new ArrayList<>();
        List<Vehicle> buses = new ArrayList<>();
        List<Vehicle> cars = new ArrayList<>();
        for (SnowplowPlayer sp : snowplowPlayers.values())
            snowplows.addAll(sp.getVehicles());
        for (BusPlayer bp : busPlayers.values())
            buses.addAll(bp.getVehicles());
        for (CarPlayer cp : carPlayers.values())
            cars.addAll(cp.getVehicles());
        System.out.println("---Hókotrók: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : snowplows)
            printVehicle(v);
        System.out.println("---Buszok: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : buses)
            printVehicle(v);
        System.out.println("---Autók: (vehicleID, laneID/junctionID)---");
        for (Vehicle v : cars)
            printVehicle(v);
    }

    private void printVehicle(Vehicle v) {
        String vid = v.getId();
        String locId = v.getLocation() != null ? v.getLocation().getId() : "null";
        System.out.printf("%s, %s\n", vid, locId);
    }

    public void printPlayers() {
        System.out.println("---Játékosok: (playerID) ---");
        for (String pid : busPlayers.keySet())
            System.out.println(pid);
        for (String pid : snowplowPlayers.keySet())
            System.out.println(pid);
    }

    /** Creates and registers a new Junction in the road network. */
    public void newJunction() {
        if (rn == null) {
            System.out.println("Game state uninitialized");
            return;
        }
        String id = validateNewId("junc_" + (junctions.size() + 1));
        Junction j = new Junction();
        j.setRoadNetwork(rn);
        rn.addJunction(j);
        j.setId(id);
        junctions.put(id, j);
        idMap.put(j, id);
    }

    /**
     * Creates and registers a new Lane in the road network.
     *
     * @param args args[0] = start junction id, args[1] = end junction id, args[2] =
     *             length
     */
    public void newLane(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments");
            return;
        }
        String startId = args[0];
        String endId = args[1];
        int length;
        try {
            length = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument format");
            return;
        }

        Junction start = junctions.get(startId);
        Junction end = junctions.get(endId);
        if (start == null || end == null) {
            printJunctions();
            return;
        }

        Lane l = new Lane(start, end, length);
        l.setRoadNetwork(rn);
        l.setSurface(new SmallSnow(l));
        rn.addLane(l);
        start.addStarting(l);
        end.addEnding(l);

        String id = validateNewId("lane_" + (lanes.size() + 1));
        l.setId(id);
        lanes.put(id, l);
        idMap.put(l, id);
    }

    /**
     * Creates and registers a new Building attached to a junction.
     *
     * @param args args[2] = junction id
     */
    public void newBuilding(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments");
            return;
        }
        Junction j = junctions.get(args[2]);
        if (j == null) {
            printJunctions();
            return;
        }
        Building b = new Building(j);
        j.addBuilding(b);
        String id = validateNewId("bldg_" + (buildings.size() + 1));
        b.setId(id);
        buildings.put(id, b);
        idMap.put(b, id);
    }

    /**
     * Creates and registers a new CarPlayer with its associated Car vehicle.
     *
     * @param args args[2] = home id, args[3] = work id
     */
    public void newCarPlayer(String[] args) {
        if (args.length < 4) {
            System.out.println("Not enough arguments");
            return;
        }
        Building home = buildings.get(args[2]);
        Building work = buildings.get(args[3]);
        if (home == null || work == null) {
            printBuildings();
            return;
        }
        CarPlayer cp = new CarPlayer(home, work);
        String pid = validateNewId("player_car_" + (carPlayers.size() + 1));
        cp.setId(pid);
        carPlayers.put(pid, cp);
        idMap.put(cp, pid);

        Car c = cp.getCar();
        c.setRecoverer(recoverer);
        c.setLocation(home.getConnection());

        String vid = validateNewId("veh_car_" + (vehicles.size() + 1));
        c.setId(vid);
        vehicles.put(vid, c);
        idMap.put(c, vid);
    }

    /**
     * Creates and registers a new SnowplowPlayer with an initial Snowplow.
     *
     * @param args args[2] = starting junction id
     */
    public void newSnowplowPlayer(String[] args) {
        if (args.length < 3) {
            System.out.println("Not enough arguments");
            return;
        }
        Junction j = junctions.get(args[2]);
        if (j == null) {
            printJunctions();
            return;
        }
        SnowplowPlayer sp = new SnowplowPlayer(j);
        sp.setWarehouse(warehouse);

        String pid = validateNewId("player_sp_" + (snowplowPlayers.size() + 1));
        sp.setId(pid);
        snowplowPlayers.put(pid, sp);
        idMap.put(sp, pid);

        Snowplow s = sp.getSnowplow(0);
        String cid = validateNewId("veh_sp_" + (vehicles.size() + 1));
        s.setId(cid);
        vehicles.put(cid, s);
        idMap.put(s, cid);

        Equipment eq = s.getCurrentEquipment();
        if (eq != null) {
            String eid = validateNewId(
                    "eq_" + eq.getClass().getSimpleName().toLowerCase() + "_" + (equipments.size() + 1));
            eq.setId(eid);
            equipments.put(eid, eq);
            idMap.put(eq, eid);
        }
    }

    /**
     * Creates and registers a new BusPlayer with its associated Bus vehicle.
     *
     * @param args args[2] = b1 id, args[3] = b2 id
     */
    public void newBusPlayer(String[] args) {
        if (args.length < 4) {
            System.out.println("Not enough arguments");
            return;
        }
        Building b1 = buildings.get(args[2]);
        Building b2 = buildings.get(args[3]);
        if (b1 == null || b2 == null) {
            printBuildings();
            return;
        }
        BusPlayer bp = new BusPlayer(b1.getConnection());
        String pid = validateNewId("player_bus_" + (busPlayers.size() + 1));
        bp.setId(pid);
        busPlayers.put(pid, bp);
        idMap.put(bp, pid);

        Bus b = bp.getBus();
        List<Building> stations = new ArrayList<>();
        stations.add(b1);
        stations.add(b2);
        b.setStations(stations);

        String vid = validateNewId("veh_bus_" + (vehicles.size() + 1));
        b.setId(vid);
        vehicles.put(vid, b);
        idMap.put(b, vid);
    }

    /**
     * Changes the active player.
     *
     * @param args args[1] = player id
     */
    public void change(String[] args) {
        if (args.length > 1) {
            if (busPlayers.containsKey(args[1]) || snowplowPlayers.containsKey(args[1]))
                activePlayerId = args[1];
            else {
                printPlayers();
            }
        } else {
            System.out.println("Not enough arguments");
        }
    }

    /** Prints the current game status (scores, positions, etc.) to stdout. */
    public void status() {
        if (activePlayerId == null) {
            System.out.println("No active player");
            return;
        }

        SnowplowPlayer sp = snowplowPlayers.get(activePlayerId);
        if (sp != null) {
            int balance = bank != null ? bank.getBalance(sp) : 0;
            System.out.print(balance);
            for (Snowplow s : sp.getSnowplows()) {
                String vid = s.getId();
                String eid = s.getCurrentEquipment() != null ? s.getCurrentEquipment().getId() : "null";
                System.out.print(", " + vid + ", " + eid);
            }
            System.out.println("");
            return;
        }

        BusPlayer bp = busPlayers.get(activePlayerId);
        if (bp != null) {
            int balance = bank != null ? bank.getBalance(bp) : 0;
            Bus b = bp.getBus();
            String vid = b.getId();
            List<Building> stations = b.getStations();
            String s1 = stations.size() > 0 && stations.get(0).getConnection() != null
                    ? stations.get(0).getConnection().getId()
                    : "null";
            String s2 = stations.size() > 1 && stations.get(1).getConnection() != null
                    ? stations.get(1).getConnection().getId()
                    : "null";
            System.out.printf("(%s, %s, %s, %d, %b)\n", vid, s1, s2, balance, b.isCrashed());
            return;
        }

        System.out.println("No active player");
    }

    /**
     * Moves a vehicle to a specified destination.
     *
     * @param args args[1] = destination id, optionally -v <index>
     */
    public void move(String[] args) {
        if (activePlayerId == null) {
            System.out.println("No active player");
            return;
        }
        Player p = carPlayers.get(activePlayerId);
        if (p == null)
            p = busPlayers.get(activePlayerId);
        if (p == null)
            p = snowplowPlayers.get(activePlayerId);

        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }
        String destId = args[1];
        Junction dest = junctions.get(destId);

        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument format");
                return;
            }
        }

        Vehicle[] playerVehicles = p.getVehicles().toArray(new Vehicle[0]);
        if (vIdx < 0 || vIdx >= playerVehicles.length) {
            System.out.println("Invalid vehicle index");
            return;
        }
        MapComponent loc = playerVehicles[vIdx].getLocation();
        if (loc != null && lanes.containsKey(loc.getId())) {
            System.out.println("In lane");
            return;
        }

        List<Junction> available = new ArrayList<>();
        if (loc != null) {
            Junction currJunc = junctions.get(loc.getId());
            if (currJunc != null) {
                for (Lane l : currJunc.getLanes()) {
                    available.add(l.getEnd());
                }
            }
        }

        if (!available.contains(dest)) {
            System.out.println("---Elérhető kereszteződések: (junctionID)---");
            for (Junction junction : available) {
                System.out.println(junction.getId());
            }
        } else {
            p.choseDirection(dest, vIdx);
        }
    }

    /**
     * Processes a purchase request by a player.
     *
     * @param args args[1] = item id/name
     */
    public void purchase(String[] args) {
        if (activePlayerId == null) {
            System.out.println("No active player");
            return;
        }
        SnowplowPlayer sp = snowplowPlayers.get(activePlayerId);
        if (sp == null) {
            System.out.println("No active snowplow player");
            return;
        }

        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }
        String item = args[1];
        if (item.equalsIgnoreCase("Snowplow")) {
            sp.buySnowplow();
            List<Snowplow> plows = sp.getSnowplows();
            if (!plows.isEmpty()) {
                Snowplow newPlow = plows.get(plows.size() - 1);
                if (!vehicles.containsValue(newPlow)) {
                    String cid = validateNewId("veh_sp_" + (vehicles.size() + 1));
                    newPlow.setId(cid);
                    vehicles.put(cid, newPlow);
                    idMap.put(newPlow, cid);
                }
            }
        } else {
            int id = -1;
            if (item.equalsIgnoreCase("Sweeper"))
                id = 1;
            else if (item.equalsIgnoreCase("Impeller"))
                id = 2;
            else if (item.equalsIgnoreCase("Salter"))
                id = 3;
            else if (item.equalsIgnoreCase("IceBreaker"))
                id = 4;
            else if (item.equalsIgnoreCase("DragonBlade"))
                id = 5;

            if (id != -1) {
                int oldSize = warehouse.getStock().size();
                sp.buyEquipment(id);
                if (warehouse.getStock().size() > oldSize) {
                    Equipment newEq = warehouse.getStock().get(warehouse.getStock().size() - 1);
                    String eid = validateNewId("eq_" + item.toLowerCase() + "_" + (equipments.size() + 1));
                    newEq.setId(eid);
                    equipments.put(eid, newEq);
                    idMap.put(newEq, eid);
                }
            } else {
                System.out.println("Invalid item");
            }
        }
    }

    /**
     * Equips a piece of equipment onto a snowplow from the warehouse.
     *
     * @param args args[1] = equipment name
     */
    public void equip(String[] args) {
        if (activePlayerId == null) {
            System.out.println("No active player");
            return;
        }
        SnowplowPlayer sp = snowplowPlayers.get(activePlayerId);
        if (sp == null) {
            System.out.println("No active snowplow player");
            return;
        }

        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }
        String eqName = args[1];
        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument format");
                return;
            }
        }
        if (vIdx < 0 || vIdx >= sp.getSnowplows().size()) {
            System.out.println("Invalid vehicle index");
            return;
        }
        Snowplow plow = sp.getSnowplow(vIdx);

        MapComponent loc = plow.getLocation();
        if (loc == null || warehouse == null || warehouse.getConnection() == null
                || !loc.getId().equals(warehouse.getConnection().getId())) {
            System.out.println("A kotró nincs a raktárban");
            return;
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

    /**
     * Creates a new surface on a given lane.
     */
    public void newSurface(String[] args) {
        if (args.length < 7) {
            System.out.println("Not enough arguments");
            return;
        }
        Lane l = lanes.get(args[2]);
        if (l == null) {
            for (String jid : junctions.keySet()) {
                System.out.println("---Kereszteződések: (junctionID)---");
                System.out.println(jid);
            }
            return;
        }
        String type = args[3];
        String modStr = args[4];
        int snow;
        int ice;
        try {
            snow = Integer.parseInt(args[5]);
            ice = Integer.parseInt(args[6]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid argument format");
            return;
        }

        Modifier mod = modStr.equals("Salted") ? new Salted() : new Unmodified();
        Surface surf;
        switch (type) {
            case "SmallSnow":
                surf = new SmallSnow(l, mod);
                break;
            case "DeepSnow":
                surf = new DeepSnow(l, mod);
                break;
            case "Ice":
                surf = new Ice(l, mod);
                break;
            case "Grit":
                surf = new Grit(l, mod);
                break;
            default:
                surf = new SmallSnow(l, mod);
                break;
        }
        surf.addSnow(snow);
        surf.addIce(ice);
        l.setSurface(surf);
    }

    /**
     * Causes a vehicle to progress on its current lane.
     */
    public void progress(String[] args) {
        if (activePlayerId == null) {
            System.out.println("No active player");
            return;
        }

        int vIdx = 0;
        if (args.length > 3 && args[2].equals("-v")) {
            try {
                vIdx = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid argument format");
                return;
            }
        }

        SnowplowPlayer sp = snowplowPlayers.get(activePlayerId);
        if (sp != null) {
            if (vIdx < 0 || vIdx >= sp.getSnowplows().size()) {
                System.out.println("Invalid vehicle index");
                return;
            }
            Snowplow plow = sp.getSnowplow(vIdx);
            if (plow.getLocation() != null) {
                plow.getLocation().progress(plow);
            }
            return;
        }

        BusPlayer bp = busPlayers.get(activePlayerId);
        if (bp != null) {
            if (vIdx != 0) {
                System.out.println("Invalid vehicle index");
                return;
            }
            Bus bus = bp.getBus();
            if (bus.getLocation() != null) {
                bus.getLocation().progress(bus);
            }
            return;
        }

        System.out.println("No active player or invalid player type for progress");
    }

    // -------------------------------------------------------------------------
    // Getters used by StateSerializer
    // -------------------------------------------------------------------------

    /** Returns the Bank managing all player balances, or null if not set. */
    public Bank getBank() {
        return bank;
    }

    /** Returns the Warehouse building, or null if not set. */
    public Warehouse getWarehouse() {
        return warehouse;
    }

    /** Returns the global Recoverer, or null if not set. */
    public Recoverer getRecoverer() {
        return recoverer;
    }

    /** Returns the live list of all Junction objects in the network. */
    public List<Junction> getJunctions() {
        return new ArrayList<>(junctions.values());
    }

    /** Returns the live list of all Building objects. */
    public List<Building> getBuildings() {
        return new ArrayList<>(buildings.values());
    }

    /** Returns the live list of all Lane objects. */
    public List<Lane> getLanes() {
        return new ArrayList<>(lanes.values());
    }

    /** Returns the live list of all CarPlayer objects. */
    public List<CarPlayer> getCarPlayers() {
        return new ArrayList<>(carPlayers.values());
    }

    /** Returns the live list of all BusPlayer objects. */
    public List<BusPlayer> getBusPlayers() {
        return new ArrayList<>(busPlayers.values());
    }

    /** Returns the live list of all SnowplowPlayer objects. */
    public List<SnowplowPlayer> getSnowplowPlayers() {
        return new ArrayList<>(snowplowPlayers.values());
    }

    /** Returns the live list of every Equipment piece in the game. */
    public List<Equipment> getAllEquipments() {
        return new ArrayList<>(equipments.values());
    }

    // -------------------------------------------------------------------------
    // Setters used by StateParser during load
    // -------------------------------------------------------------------------

    /**
     * Sets the bank. Called by StateParser after building it from JSON.
     *
     * @param bank the bank to set
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }

    /**
     * Sets the warehouse singleton. Called by StateParser.
     *
     * @param warehouse the warehouse to set
     */
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * Sets the recoverer singleton. Called by StateParser.
     *
     * @param recoverer the recoverer to set
     */
    public void setRecoverer(Recoverer recoverer) {
        this.recoverer = recoverer;
    }

    public void addJunction(Junction j) {
        junctions.put(j.getId(), j);
        idMap.put(j, j.getId());
    }

    public void addBuilding(Building b) {
        buildings.put(b.getId(), b);
        idMap.put(b, b.getId());
    }

    public void addLane(Lane l) {
        lanes.put(l.getId(), l);
        idMap.put(l, l.getId());
    }

    public void addCarPlayer(CarPlayer p) {
        carPlayers.put(p.getId(), p);
        idMap.put(p, p.getId());
    }

    public void addBusPlayer(BusPlayer p) {
        busPlayers.put(p.getId(), p);
        idMap.put(p, p.getId());
    }

    public void addSnowplowPlayer(SnowplowPlayer p) {
        snowplowPlayers.put(p.getId(), p);
        idMap.put(p, p.getId());
    }

    public void addEquipment(Equipment eq) {
        equipments.put(eq.getId(), eq);
        idMap.put(eq, eq.getId());
    }

    public void addVehicle(Vehicle v) {
        vehicles.put(v.getId(), v);
        idMap.put(v, v.getId());
    }

    /**
     * Clears all game objects and resets singleton references to null.
     * Called by StateParser at the start of a load to start from a clean slate.
     */
    public void clearAll() {
        junctions.clear();
        buildings.clear();
        lanes.clear();
        carPlayers.clear();
        busPlayers.clear();
        snowplowPlayers.clear();
        equipments.clear();
        vehicles.clear();
        idMap.clear();
        bank = null;
        warehouse = null;
        recoverer = null;
        rn = null;
        activePlayerId = null;
    }
}
