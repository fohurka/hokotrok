import java.util.ArrayList;
import java.util.List;

public class Recoverer {
    private List<Car> recoveryQueue = new ArrayList<>();

    /**
     * Adds a car to the recovery queue to be processed later.
     * This method is typically called when a car crashes and needs
     * assistance from the recovery service during the next simulation tick.
     * @param c The car object that requires recovery.
     */
    public void addToRecoveryQueue(Car c) {
        recoveryQueue.add(c);
    }

    /**
     * Tries to recover crashed cars
     */
    public void tick() {
        if (!recoveryQueue.isEmpty()) {
            Car c = recoveryQueue.remove(0);
            c.recover();
        }
    }
}