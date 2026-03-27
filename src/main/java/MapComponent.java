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

    public void remove(Vehicle v) {
        Skeleton.printFunctionCall("MapComponent.remove");
        vehicles.remove(v);
        Skeleton.printReturn();
    }

    public void arrived(Vehicle v) {
        Skeleton.printFunctionCall("MapComponent.arrived");
        vehicles.add(v);
        Skeleton.printReturn();
    }

    public abstract void progress(CivilVehicle cv);

    public abstract void progress(Snowplow sp);

    public void crashRecovered() {}
}
