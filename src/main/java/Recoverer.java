import java.util.ArrayList;
import java.util.List;

public class Recoverer {
    private List<Car> recoveryQueue;

    public Recoverer() {
        recoveryQueue = new ArrayList<>();
    }

    public void addToRecoveryQueue(Car c) {
        recoveryQueue.add(c);
    }
}
