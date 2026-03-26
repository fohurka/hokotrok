public class Car extends CivilVehicle {
    private Recoverer rec;

    public Car(Skeleton s) {
        super(s);
    }

    public void setRecoverer(Recoverer r) {
        rec = r;
    }

    public void tick() {
        getLocation().progress(this);
    }

    public boolean pushable() {
        return true;
    }

    @Override
    public void stuckInCurrentLane(Lane lane) {
        Lane neighbor = lane.getRightNeighbor();
        boolean enterable = neighbor.enterable();
        if (enterable) {
            setLocation(neighbor);
        }
    }

    @Override
    public void slip(Lane lane) {
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            rec.addToRecoveryQueue(this);
            lane.crashHappened();
        }
    }

    @Override
    public void crash() {
        rec.addToRecoveryQueue(this);
    }
}