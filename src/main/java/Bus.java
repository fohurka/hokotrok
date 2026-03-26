public class Bus extends CivilVehicle {

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
        Skeleton.printFunctionCall("Bus.stuckInCurrentLane");
        Skeleton.printReturn();
    }

    @Override
    public void slip(Lane lane) {
        Skeleton.printFunctionCall("Bus.slip");
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            lane.crashHappened();
        }
        Skeleton.printReturn();
    }

    @Override
    public void crash() {
        Skeleton.printFunctionCall("Bus.crash");
        Skeleton.printReturn();
    }
}
