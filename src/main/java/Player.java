import java.util.ArrayList;
import java.util.List;
/**
 * Represents an abstract player in the game.
 * Players are responsible for choosing the direction of vehicles.
 * Each player controls a different type of vehicle.
 * // TODO MORE INFO
 */
public abstract class Player {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    /**
     * Chooses the next direction(Lane or Building) for a vehicle to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the vehicle to control.
     */
    public abstract void choseDirection(MapComponent dest, int index);

    public abstract List<Vehicle> getVehicles();

}