import java.util.List;

public class Snowplow extends Vehicle {
    private Equipment eq;

    public void setEquipment(Equipment eq) {
        this.eq = eq;
    }

    @Override
    public void tick() {
        Skeleton.printFunctionCall("Snowplow.tick");
        getLocation().progress(this);
        Skeleton.printReturn();
    }

    @Override
    public boolean pushable() {
        return false;
    }

    public void laneCleared(Lane lane, MapComponent end) {
        Skeleton.printFunctionCall("Snowplow.laneCleared");
        List<Vehicle> pushables = lane.getPushableCars();
        for (Vehicle v : pushables) {
            v.setLocation(end);
        }
        eq.use(lane);
        setLocation(end);
        Skeleton.printReturn();
    }

    public void crash() {
    }
}
