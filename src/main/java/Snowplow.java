import java.util.List;

public class Snowplow extends Vehicle {
    private Equipment eq;

    public Snowplow(Skeleton s) {
        super(s);
    }

    public void setEquipment(Equipment eq) {
        this.eq = eq;
    }

    @Override
    public void tick() {
        getLocation().progress(this);
    }

    @Override
    public boolean pushable() {
        return false;
    }

    public void laneCleared(Lane lane, MapComponent end) {
        List<Vehicle> pushables = lane.getPushableCars();
        for (Vehicle v : pushables) {
            v.setLocation(end);
        }
        eq.use(lane);
        setLocation(end);
    }

    public void crash() {}
}
