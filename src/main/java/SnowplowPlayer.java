import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player who controls snowplows in the game.
 */
public class SnowplowPlayer extends Player {

    private List<Snowplow> snowplows;
    private Warehouse warehouse;

    private SnowplowPlayer() {
        this.snowplows = new ArrayList<>();
    }

    /**
     * Creates an empty SnowplowPlayer without a default snowplow.
     * Used exclusively by the state parser during game state load.
     *
     * @return A new empty SnowplowPlayer instance.
     */
    public static SnowplowPlayer createEmpty() {
        return new SnowplowPlayer();
    }

    /**
     * Constructs a new SnowplowPlayer and initializes a snowplow at the given
     * junction.
     *
     * @param starter The junction where the initial snowplow spawns.
     */
    public SnowplowPlayer(Junction starter) {
        this();
        snowplows.add(new Snowplow());
        snowplows.get(0).setLocation(starter);
        Equipment defaultEq = new Sweeper(this);
        snowplows.get(0).setEquipment(defaultEq);
    }

    /**
     * Gets a specific snowplow controlled by this player.
     *
     * @param index The index of the snowplow to retrieve.
     * @return The snowplow at the specified index.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public Snowplow getSnowplow(int index) {
        if (index < 0 || index >= snowplows.size()) {
            throw new IllegalArgumentException("Invalid snowplow index");
        }
        return snowplows.get(index);
    }

    /**
     * Chooses the next direction(Lane or Building) for a specific snowplow to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the snowplow to command.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    @Override
    public void choseDirection(MapComponent dest, int index) {
        if (index < 0 || index >= snowplows.size()) {
            throw new IllegalArgumentException("Invalid snowplow index");
        }
        snowplows.get(index).setLocation(dest);
    }

    /**
     * Sets the warehouse associated with the player, from which they can purchase
     * items.
     *
     * @param warehouse The warehouse object to be set.
     */
    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * The player attempts to buy a new snowplow equipment (blade) from the
     * warehouse.
     * In the skeleton, it forwards the call to the warehouse's respective buying
     * method.
     *
     * @param id The ID of the equipment to be purchased.
     */
    public void buyEquipment(int id) {
        if (warehouse != null) {
            Equipment newEq = warehouse.buyEquipment(this, id);
        }
    }

    /**
     * Adds a given snowplow to this player's fleet.
     *
     * @param sp The snowplow to add.
     */
    public void addSnowplow(Snowplow sp) {
        snowplows.add(sp);
    }

    /**
     * The player attempts to buy a new snowplow vehicle from the warehouse.
     * In the skeleton, it forwards the call to the warehouse's respective buying
     * method.
     */
    public void buySnowplow() {
        if (warehouse != null) {
            Snowplow newPlow = warehouse.buySnowplow(this);
            if (newPlow != null) {
                addSnowplow(newPlow);
            }
        }
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        for (Snowplow sp : snowplows) {
            vehicles.add(sp);
        }
        return vehicles;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public List<Snowplow> getSnowplows() {
        return snowplows;
    }

}