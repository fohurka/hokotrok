import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for recovering crashed cars.
 * It maintains a queue of cars that need recovery and processes them one by one during each tick.
 */
public class Recoverer {
    private List<Car> recoveryQueue = new ArrayList<>();

    /**
     * Adds a car to the recovery queue to be processed later.
     * This method is typically called when a car crashes and needs
     * assistance from the recovery service.
     * 
     * @param c The car object that requires recovery.
     */
    public void addToRecoveryQueue(Car c) {
        recoveryQueue.add(c);
    }

    /**
     * Processes the recovery queue. If there are cars waiting for recovery,
     * the one at the front of the queue is recovered.
     */
    public void tick() {
        if (!recoveryQueue.isEmpty()) {
            Car c = recoveryQueue.remove(0);
            c.recover();
        }
    }

    /**
     * Returns the current list of cars waiting for recovery.
     * 
     * @return The recovery queue as a list of cars.
     */
    public List<Car> getRecoveryQueue() {
        return recoveryQueue;
    }
}