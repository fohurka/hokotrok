import java.util.ArrayList;
import java.util.List;

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
        loc.progress(this);
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
    }

    /**
     * Gets recovered by the Recoverer
     */
    public void recover() {
        if (loc != null) {
            loc.crashRecovered();
        }
        owner.goHome();
    }

    public CarPlayer getOwner() {
        return owner;
    }
}