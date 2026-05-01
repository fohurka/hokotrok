import java.util.List;
import java.util.Map;

/**
 * Container for all Data Transfer Objects used in game state
 * serialization and deserialization.
 * Each inner DTO class mirrors exactly one JSON object from the game state
 * schema.
 */
public class GameState {

    // -------------------------------------------------------------------------
    // Top-level root DTO
    // -------------------------------------------------------------------------

    /**
     * Root DTO that wraps the entire game state.
     * Produced by StateSerializer and consumed by StateParser.
     * Mirrors the root "gameState" JSON key.
     */
    public static class GameStateDto {
        public BankDto bank;
        public WarehouseDto warehouse;
        public RecovererDto recoverer;
        public MapDto map;
        public List<CarPlayerDto> carPlayers;
        public List<BusPlayerDto> busPlayers;
        public List<SnowplowPlayerDto> snowplowPlayers;
        public List<CarDto> cars;
        public List<BusDto> buses;
        public List<SnowplowDto> snowplows;
        public List<EquipmentDto> equipments;
    }

    // -------------------------------------------------------------------------
    // Map structure
    // -------------------------------------------------------------------------

    /**
     * DTO for the "map" section, grouping all topology objects.
     * Holds the canonical lists of junctions, buildings, and lanes that
     * form the road network. Each element carries its own id so that
     * cross-references can be resolved by the parser.
     */
    public static class MapDto {
        public List<JunctionDto> junctions;
        public List<BuildingDto> buildings;
        public List<LaneDto> lanes;
    }

    /**
     * DTO for a single junction in the road network.
     * A junction is a graph node. Lanes starting here are in startingLaneIds,
     * lanes ending here are in endingLaneIds, and adjacent buildings are in
     * buildingIds.
     */
    public static class JunctionDto {
        public String id;
        public List<String> startingLaneIds;
        public List<String> endingLaneIds;
        public List<String> buildingIds;
    }

    /**
     * DTO for a single building attached to a junction.
     * The type field is either "Normal" for a generic building
     * or "Warehouse" for the equipment warehouse.
     */
    public static class BuildingDto {
        public String id;
        public String connectionId;
        public String type; // "Normal" or "Warehouse"
    }

    /**
     * DTO for a single directed lane connecting two junctions.
     * The vehicles list encodes each vehicle's progress position as a
     * single-key map of vehicleId -> progressPosition.
     * Neighbor IDs are null when the lane has no neighbor in that direction.
     */
    public static class LaneDto {
        public String id;
        public int length;
        public String startJunctionId;
        public String endJunctionId;
        public String leftNeighborId; // null when no left neighbor
        public String rightNeighborId; // null when no right neighbor
        public List<Map<String, Integer>> vehicles; // vehicleId -> progressPosition
        public boolean isCrashed;
        public SurfaceDto surface;
    }

    // -------------------------------------------------------------------------
    // Surface
    // -------------------------------------------------------------------------

    /**
     * DTO for the surface conditions of a single lane.
     * The type field selects which Surface subclass to reconstruct on load:
     * "SmallSnow", "DeepSnow", "Ice", "Grit", or "Unmodified".
     * The modifier field encodes the active modifier: "Salted" or "Unmodified".
     */
    public static class SurfaceDto {
        public String type; // "SmallSnow", "DeepSnow", "Ice", "Grit", "Unmodified"
        public int snowAmount;
        public int iceAmount;
        public int snowThreshold;
        public int iceThreshold;
        public String modifier; // "Salted" or "Unmodified"
    }

    // -------------------------------------------------------------------------
    // Vehicles
    // -------------------------------------------------------------------------

    /**
     * DTO encoding the current location of a vehicle.
     * The type field is "Lane" or "Junction"; id references the corresponding
     * object.
     */
    public static class LocationDto {
        public String type; // "Lane" or "Junction"
        public String id;
    }

    /**
     * DTO for a Car vehicle.
     * isCrashed is derived during serialization from the Recoverer recovery queue,
     * not from a dedicated flag on Car itself. The recoverer reference is not
     * serialized; it is wired back automatically during deserialization.
     */
    public static class CarDto {
        public String id;
        public final String type = "Car";
        public String ownerId;
        public LocationDto location;
        public boolean isCrashed;
        // recovererId is NOT serialized (always the single global recoverer)
    }

    /**
     * DTO for a Bus vehicle.
     * isCrashed maps directly to the Bus crashed state.
     */
    public static class BusDto {
        public String id;
        public final String type = "Bus";
        public String ownerId;
        public LocationDto location;
        public boolean isCrashed;
    }

    /**
     * DTO for a Snowplow vehicle.
     * equipmentId is null when no equipment is currently mounted on the snowplow.
     */
    public static class SnowplowDto {
        public String id;
        public final String type = "Snowplow";
        public String ownerId;
        public LocationDto location;
        public String equipmentId; // null if no equipment is equipped
    }

    // -------------------------------------------------------------------------
    // Players
    // -------------------------------------------------------------------------

    /**
     * DTO for a CarPlayer.
     * References the controlled vehicle via vehicleId, and home and work
     * buildings via homeId and workId.
     */
    public static class CarPlayerDto {
        public String id;
        public final String type = "CarPlayer";
        public String vehicleId;
        public String homeId;
        public String workId;
    }

    /**
     * DTO for a BusPlayer.
     * stop1 and stop2 are the IDs of the two target building stations.
     * Both are null when no stops have been assigned yet.
     */
    public static class BusPlayerDto {
        public String id;
        public final String type = "BusPlayer";
        public String vehicleId;
        public String stop1; // null when no stop is assigned
        public String stop2; // null when no stop is assigned
    }

    /**
     * DTO for a SnowplowPlayer.
     * May control multiple snowplows; all vehicle IDs are listed in vehicleIds.
     * The warehouse reference is stored by ID so the parser can look it up
     * after all buildings have been reconstructed.
     */
    public static class SnowplowPlayerDto {
        public String id;
        public final String type = "SnowplowPlayer";
        public List<String> vehicleIds;
        public String warehouseId;
    }

    // -------------------------------------------------------------------------
    // Bank
    // -------------------------------------------------------------------------

    /**
     * DTO for the Bank.
     * Accounts are stored as a map of playerId -> balance so that balances
     * can be restored by player ID without needing ordered lists.
     */
    public static class BankDto {
        public Map<String, Integer> accounts; // playerId -> balance
    }

    // -------------------------------------------------------------------------
    // Warehouse
    // -------------------------------------------------------------------------

    /**
     * DTO for the Warehouse building.
     * The stock list contains the IDs of all equipment pieces currently stored
     * in the warehouse and not equipped on any snowplow.
     */
    public static class WarehouseDto {
        public String id;
        public List<String> stock; // equipment ids currently in stock
    }

    // -------------------------------------------------------------------------
    // Recoverer
    // -------------------------------------------------------------------------

    /**
     * DTO for the Recoverer.
     * recoveryQueue lists the IDs of crashed Car vehicles awaiting recovery,
     * in queue order.
     */
    public static class RecovererDto {
        public String id;
        public List<String> recoveryQueue; // car ids in the recovery queue
    }

    // -------------------------------------------------------------------------
    // Equipment
    // -------------------------------------------------------------------------

    /**
     * DTO for any Equipment piece.
     *
     * The type field identifies the concrete subclass:
     * "Salter", "DragonBlade", "Gritter", "Sweeper", "Impeller", "IceBreaker"
     *
     * ownerId is null when the equipment is in the warehouse stock and not
     * assigned to any snowplow player.
     *
     * stateData holds type-specific persistent state:
     * Salter, DragonBlade, Gritter: {"ammo": <int>}
     * All others: {} (empty)
     */
    public static class EquipmentDto {
        public String id;
        public String type; // "Salter", "DragonBlade", "Gritter", "Sweeper", "Impeller", "IceBreaker"
        public String ownerId; // null when in warehouse stock (no owner)
        public Map<String, Object> stateData;
    }
}
