import java.util.List;
/**
 * Represents an abstract player in the game.
 * Players are responsible for choosing the direction of vehicles and manage a set of vehicles.
 * Each player controls a different type of vehicle.
 */
public abstract class Player {

    protected String id;

    /**
     * Returns the unique identifier of the player.
     * 
     * @return The player's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the player.
     * 
     * @param id The ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Chooses the next direction (Lane or Building) for a vehicle to take.
     *
     * @param dest  The destination map component where the vehicle should go.
     * @param index The index of the vehicle to control.
     */
    public abstract void choseDirection(MapComponent dest, int index);

    /**
     * Returns a list of vehicles controlled by this player.
     * 
     * @return A list of vehicles.
     */
    public abstract List<Vehicle> getVehicles();

}