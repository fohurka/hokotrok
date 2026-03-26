import java.util.ArrayList;
import java.util.List;

public abstract class MapComponent {
    private List<Vehicle> vehicles;
    protected final Skeleton s;

    protected List<Vehicle> getVehicles() {
        return vehicles;
    }

    public MapComponent(Skeleton s) {
        this.s = s;
        vehicles = new ArrayList<>();
    }

    public void remove(Vehicle v) {
        vehicles.remove(v);
    }

    public void arrived(Vehicle v) {
        vehicles.add(v);
    }

    public abstract void progress(CivilVehicle cv);
    public abstract void progress(Snowplow sp);
}
