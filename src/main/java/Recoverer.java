import java.util.ArrayList;
import java.util.List;

public class Recoverer {
    private List<Car> recoveryQueue;
    private final Skeleton s;

    public Recoverer(Skeleton s) {
        this.s = s;
        recoveryQueue = new ArrayList<>();
    }

    public void addToRecoveryQueue(Car c) {
        recoveryQueue.add(c);
    }
}
