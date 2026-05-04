import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for components of the game map, such as lanes and junctions.
 * It manages the vehicles currently present on the component.
 */
public abstract class MapComponent {
    protected String id;
    protected List<Vehicle> vehicles;

    /**
     * Returns the list of vehicles currently on this map component.
     * 
     * @return A list of vehicles.
     */
    protected List<Vehicle> getVehicles() {
        return vehicles;
    }

    /**
     * Returns the unique identifier of the map component.
     * 
     * @return The component's ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the map component.
     * 
     * @param id The ID to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Constructs a MapComponent and initializes the list of vehicles.
     */
    public MapComponent() {
        vehicles = new ArrayList<>();
    }

    /**
     * Removes a vehicle from this map component.
     * This is typically called when a vehicle moves out of the current component.
     * 
     * @param v The vehicle to be removed.
     */
    public void remove(Vehicle v) {
        vehicles.remove(v);
    }

    /**
     * Registers that a vehicle has arrived at this map component.
     * Adds the vehicle to the internal list of currently present vehicles.
     * 
     * @param v The vehicle that has entered this component.
     */
    public void arrived(Vehicle v) {
        vehicles.add(v);
    }

    /**
     * Progresses a civil vehicle on this component.
     * 
     * @param cv The civil vehicle to progress.
     */
    public abstract void progress(CivilVehicle cv);

    /**
     * Progresses a snowplow on this component.
     * 
     * @param sp The snowplow to progress.
     */
    public abstract void progress(Snowplow sp);

    /**
     * Progresses a car on this component by treating it as a civil vehicle.
     * 
     * @param c The car to progress.
     */
    public void progress(Car c){
        progress((CivilVehicle)c);
    }

    /**
     * Called when a crash on this component has been recovered.
     * Subclasses can override this to perform specific actions.
     */
    public void crashRecovered() {}
}
