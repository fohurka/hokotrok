public class Car extends CivilVehicle {
    private Recoverer rec;

    public Car(MapComponent loc) {
        super(loc);
    }

    public void setRecoverer(Recoverer r) {
        rec = r;
    }

    public void tick() {
        Skeleton.printFunctionCall("Car.tick");
        getLocation().progress(this);
        Skeleton.printReturn();
    }

    public boolean pushable() {
        return true;
    }

    @Override
    public void stuckInCurrentLane(Lane lane) {
        Skeleton.printFunctionCall("Car.stuckInCurrentLane");
        Lane neighbor = lane.getRightNeighbor();
        boolean enterable = neighbor.enterable();
        if (enterable) {
            setLocation(neighbor);
        }
        Skeleton.printReturn();
    }

    @Override
    public void slip(Lane lane) {
        Skeleton.printFunctionCall("Car.slip");
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            rec.addToRecoveryQueue(this);
            lane.crashHappened();
        }
        Skeleton.printReturn();
    }

    @Override
    public void crash() {
        Skeleton.printFunctionCall("Car.crash");
        rec.addToRecoveryQueue(this);
        Skeleton.printReturn();
    }
}