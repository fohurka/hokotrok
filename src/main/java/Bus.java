public class Bus extends CivilVehicle {
    public Bus(MapComponent loc) {
        super(loc);
    }

    public void tick() {
        Skeleton.printFunctionCall("Bus.tick");
        getLocation().progress(this);
        Skeleton.printReturn();
    }

    public boolean pushable() {
        return false;
    }

    @Override
    public void stuckInCurrentLane(Lane lane) {
    }

    @Override
    public void slip(Lane lane) {
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            lane.crashHappened();
        }
    }

    @Override
    public void crash() {
    }
}
