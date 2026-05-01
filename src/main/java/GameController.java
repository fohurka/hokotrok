import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

    private final List<Junction> junctions = new ArrayList<>();
    private final List<Building> buildings = new ArrayList<>();
    private final List<Lane> lanes = new ArrayList<>();
    private final List<CarPlayer> carPlayers = new ArrayList<>();
    private final List<BusPlayer> busPlayers = new ArrayList<>();
    private final List<SnowplowPlayer> snowplowPlayers = new ArrayList<>();
    private final List<Equipment> allEquipments = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Lifecycle commands
    // -------------------------------------------------------------------------

    /** Initialises the game to a clean default state. Currently a no-op. */
    public void init() {
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

    /** Sets the random seed used by randomised game logic. args[0] = seed. */
    public void setRand(String[] args) {
    }

    /** Advances the simulation by one tick: moves vehicles, updates surfaces. */
    public void tick() {
    }

    /** Prints a human-readable description of the road network to stdout. */
    public void printRoadNetwork() {
    }

    /** Prints the current state of all vehicles to stdout. */
    public void printVehicles() {
    }

    /** Creates and registers a new Junction in the road network. */
    public void newJunction() {
    }

    /**
     * Creates and registers a new Lane in the road network.
     *
     * @param args start junction id, end junction id, length, etc.
     */
    public void newLane(String[] args) {
    }

    /**
     * Creates and registers a new Building attached to a junction.
     *
     * @param args junction id, building type, etc.
     */
    public void newBuilding(String[] args) {
    }

    /**
     * Creates and registers a new CarPlayer with its associated Car vehicle.
     *
     * @param args home id, work id, etc.
     */
    public void newCarPlayer(String[] args) {
    }

    /**
     * Creates and registers a new SnowplowPlayer with an initial Snowplow.
     *
     * @param args starting junction id, etc.
     */
    public void newSnowplowPlayer(String[] args) {
    }

    /**
     * Creates and registers a new BusPlayer with its associated Bus vehicle.
     *
     * @param args starting junction id, etc.
     */
    public void newBusPlayer(String[] args) {
    }

    /**
     * Changes the equipment currently mounted on a snowplow.
     *
     * @param args args[0] = snowplow id, args[1] = equipment id
     */
    public void change(String[] args) {
    }

    /** Prints the current game status (scores, positions, etc.) to stdout. */
    public void status() {
    }

    /**
     * Moves a vehicle to a specified destination.
     *
     * @param args args[0] = vehicle id, args[1] = destination id
     */
    public void move(String[] args) {
    }

    /**
     * Processes a purchase request by a player.
     *
     * @param args args[0] = player id, args[1] = item id
     */
    public void purchase(String[] args) {
    }

    /**
     * Equips a piece of equipment onto a snowplow from the warehouse.
     *
     * @param args args[0] = snowplow id, args[1] = equipment id
     */
    public void equip(String[] args) {
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
        return junctions;
    }

    /** Returns the live list of all Building objects. */
    public List<Building> getBuildings() {
        return buildings;
    }

    /** Returns the live list of all Lane objects. */
    public List<Lane> getLanes() {
        return lanes;
    }

    /** Returns the live list of all CarPlayer objects. */
    public List<CarPlayer> getCarPlayers() {
        return carPlayers;
    }

    /** Returns the live list of all BusPlayer objects. */
    public List<BusPlayer> getBusPlayers() {
        return busPlayers;
    }

    /** Returns the live list of all SnowplowPlayer objects. */
    public List<SnowplowPlayer> getSnowplowPlayers() {
        return snowplowPlayers;
    }

    /** Returns the live list of every Equipment piece in the game. */
    public List<Equipment> getAllEquipments() {
        return allEquipments;
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
        junctions.add(j);
    }

    public void addBuilding(Building b) {
        buildings.add(b);
    }

    public void addLane(Lane l) {
        lanes.add(l);
    }

    public void addCarPlayer(CarPlayer p) {
        carPlayers.add(p);
    }

    public void addBusPlayer(BusPlayer p) {
        busPlayers.add(p);
    }

    public void addSnowplowPlayer(SnowplowPlayer p) {
        snowplowPlayers.add(p);
    }

    public void addEquipment(Equipment eq) {
        allEquipments.add(eq);
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
        allEquipments.clear();
        bank = null;
        warehouse = null;
        recoverer = null;
    }
}
