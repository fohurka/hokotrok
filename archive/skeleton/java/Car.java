public class Car extends CivilVehicle {
    private Recoverer rec;
    private CarPlayer owner;

    public Car(CarPlayer owner) {
        this.owner = owner;
    }


    public void setRecoverer(Recoverer r) {
        rec = r;
    }

    /**
     * Tries to progress at the current location
     */
    public void tick() {
        Skeleton.printFunctionCall("Car.tick");
        getLocation().progress(this);
        Skeleton.printReturn();
    }

    /**
     * @return whether this vehicle is pushable
     */
    public boolean pushable() {
        return true;
    }

    /**
     * Handles the logic when the Bus is stuck in the current Lane
     * Checks if the Lane's neighbor is enterable, and switches if it is
     * @param lane the current Lane
     */
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

    /**
     * Slips the vehicle and searches the Lane for another Vehicle to crash into
     * @param lane the current Lane
     */
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

    /**
     * Crashes the vehicle and adds it to the recovery queue of the Recoverer
     */
    @Override
    public void crash() {
        Skeleton.printFunctionCall("Car.crash");
        rec.addToRecoveryQueue(this);
        Skeleton.printReturn();
    }

    /**
     * Gets recovered by the Recoverer
     */
    public void recover() {
        Skeleton.printFunctionCall("Car.recover");
        loc.crashRecovered();
        owner.goHome();
        Skeleton.printReturn();
    }
}