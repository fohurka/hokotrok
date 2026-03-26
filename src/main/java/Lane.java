import java.util.ArrayList;
import java.util.List;

public class Lane extends MapComponent {
    private Surface surface;
    private final Junction start;
    private final Junction end;
    private Lane rightNeighbor;
    private Lane leftNeighbor;

    public Lane(Junction start, Junction end) {
        super();
        this.start = start;
        this.end = end;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }

    public void addNeighbors(Lane leftNeighbor, Lane rightNeighbor) {
        this.leftNeighbor = leftNeighbor;
        this.rightNeighbor = rightNeighbor;
    }

    public Lane getLeftNeighbor() {
        Skeleton.printFunctionCall("Lane.getLeftNeighbor");
        Skeleton.printReturn();
        return leftNeighbor;
    }

    public Lane getRightNeighbor() {
        Skeleton.printFunctionCall("Lane.getRightNeighbor");
        Skeleton.printReturn();
        return rightNeighbor;
    }

    public boolean enterable() {
        Skeleton.printFunctionCall("Lane.enterable");
        Skeleton.printReturn();
        return surface.enterable();
    }

    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Lane.progress");
        int prog = surface.calculateProgress(cv);
        if (prog > 0) {

            boolean ending = Skeleton.askBool("Did the vehicle reach the end of the lane?");
            if (ending) {
                cv.setLocation(end);
            }
        }
        Skeleton.printReturn();
    }

    public void progress(Snowplow sn) {
        Skeleton.printFunctionCall("Lane.progress");
        int prog = surface.calculateProgress(sn);
        boolean ending = Skeleton.askBool("Did the snowplow reach the end of the lane?");
        if (ending) {
            sn.laneCleared(this, end);
        }
        Skeleton.printReturn();
    }

    public List<Vehicle> getPushableCars() {
        Skeleton.printFunctionCall("Lane.getPushableCars");
        List<Vehicle> pushables = new ArrayList<>();

        if (surface.enterable()) {
            Skeleton.printReturn();
            return pushables;
        }

        for (Vehicle v : getVehicles()) {
            if (v.pushable()) {
                pushables.add(v);
            }
        }
        Skeleton.printReturn();
        return pushables;
    }

    public Vehicle getNearest(CivilVehicle cv) {
        Skeleton.printFunctionCall("Lane.getNearest");
        for (Vehicle v : getVehicles()) {
            if (v != cv) {
                Skeleton.printReturn();
                return v;
            }
        }
        Skeleton.printReturn();
        return null;
    }

    public void crashHappened() {
        Skeleton.printFunctionCall("Lane.crashHappened");
        Skeleton.printReturn();
    }
}
