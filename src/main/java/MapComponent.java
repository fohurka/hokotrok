import java.util.ArrayList;
import java.util.List;

public abstract class MapComponent {
    protected List<Vehicle> vehicles;

    protected List<Vehicle> getVehicles() {
        return vehicles;
    }

    public MapComponent() {
        vehicles = new ArrayList<>();
    }

    /**
     * Removes a vehicle from this map component.
     * This is typically called when a vehicle moves out of the current component
     * and into the next one.
     * * @param v The vehicle to be removed from the component's tracking list.
     */
    public void remove(Vehicle v) {
        vehicles.remove(v);
    }

    /**
     * Registers that a vehicle has arrived at this map component.
     * Adds the vehicle to the internal list of currently present vehicles.
     * * @param v The vehicle that has entered or arrived at this component.
     */
    public void arrived(Vehicle v) {
        vehicles.add(v);
    }

    public abstract void progress(CivilVehicle cv);

    public abstract void progress(Snowplow sp);

    public void crashRecovered() {}
}
