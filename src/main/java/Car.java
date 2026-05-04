

public class Car extends CivilVehicle {
    private Recoverer rec;
    private CarPlayer owner;

    /**
     * Constructs a new Car with the specified owner.
     *
     * @param owner The CarPlayer who owns this car.
     */
    public Car(CarPlayer owner) {
        this.owner = owner;
    }

    /**
     * Sets the recoverer responsible for this car.
     *
     * @param r The Recoverer object.
     */
    public void setRecoverer(Recoverer r) {
        rec = r;
    }

    /**
     * Tries to progress at the current location.
     * If not crashed, it calls the progress method of its current location.
     */
    @Override
    public void tick() {
        if (!isCrashed) loc.progress(this);
    }

    /**
     * @return whether this vehicle is pushable
     */
    @Override
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
        Lane neighbor = lane.getRightNeighbor();
        if (neighbor != null && neighbor.enterable()) {
            setLocation(neighbor);
        }
    }

    /**
     * Slips the vehicle and searches the Lane for another Vehicle to crash into
     * @param lane the current Lane
     */
    @Override
    public void slip(Lane lane) {
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            isCrashed = true;
            nearest.crash();
            if (rec != null) rec.addToRecoveryQueue(this);
            lane.crashHappened();
        }
    }

    /**
     * Crashes the vehicle and adds it to the recovery queue of the Recoverer
     */
    @Override
    public void crash() {
        if (rec != null) {
            rec.addToRecoveryQueue(this);
        }
        isCrashed = true;
    }

    /**
     * Gets recovered by the Recoverer
     */
    public void recover() {
        isCrashed = false;
        if (loc != null) {
            loc.crashRecovered();
        }
        owner.goHome();
    }

    /**
     * Retrieves the owner of the car.
     *
     * @return The CarPlayer object representing the owner.
     */
    public CarPlayer getOwner() {
        return owner;
    }
}