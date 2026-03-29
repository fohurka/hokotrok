import java.util.ArrayList;
import java.util.List;

public class Recoverer {
    private List<Car> recoveryQueue;

    public Recoverer() {
        recoveryQueue = new ArrayList<>();
    }

    /**
     * Adds a car to the recovery queue to be processed later.
     * This method is typically called when a car crashes and needs
     * assistance from the recovery service during the next simulation tick.
     * * @param c The car object that requires recovery.
     */
    public void addToRecoveryQueue(Car c) {
        Skeleton.printFunctionCall("Recoverer.addToRecoveryQueue");
        recoveryQueue.add(c);
        Skeleton.printReturn();
    }

    /**
     * Tries to recover crashed cars
     */
    public void tick() {
        Skeleton.printFunctionCall("Recoverer.tick");
        if (Skeleton.askBool("Van ütközött autó?"))
        {
            for (Car c : recoveryQueue) {
                c.recover();
            }
        }
        Skeleton.printReturn();
    }
}
