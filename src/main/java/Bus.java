public class Bus extends CivilVehicle {
    public Bus(Skeleton s) {
        super(s);
    }

    public void tick() {
        getLocation().progress(this);
    }

    public boolean pushable() {
        return false;
    }

    @Override
    public void stuckInCurrentLane(Lane lane) {}

    @Override
    public void slip(Lane lane) {
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            lane.crashHappened();
        }
    }

    @Override
    public void crash() {}
}
