import java.util.ArrayList;
import java.util.List;

public class Lane extends MapComponent {
    private Surface surface;
    private final Junction start;
    private final Junction end;
    private Lane rightNeighbor;
    private Lane leftNeighbor;
    private RoadNetwork rn;

    public Lane(Junction start, Junction end) {
        super();
        this.start = start;
        this.end = end;
    }

    /**
     * Sets the Surface of the Lane
     * @param surface the Surface to be set
     */
    public void setSurface(Surface surface) {
        this.surface = surface;
    }
    /**
     * Sets the road network for the lane, enabling it to send notifications
     * (e.g., for processing rewards after clearing snow).
     *
     * @param rn The road network to be set.
     */
    public void setRoadNetwork(RoadNetwork rn) {
        this.rn = rn;
    }
    /**
     * A snowplow clears the snow from the lane. During the skeleton testing phase,
     * this method asks the tester for the amount of snow cleared, then forwards
     * this information to the road network to credit the reward.
     *
     * @param p The snowplow player who performs the clearing and receives the reward.
     */
    public void cleared(SnowplowPlayer p) {
        Skeleton.printFunctionCall("Lane.clearSnow");

        int snowAmount = Skeleton.askInt("How much snow did the machine clear? ");

        if (rn != null) {
            rn.laneCleared(p, snowAmount);
        }
        Skeleton.printReturn();
    }
    /**
     * Sets the neighbors of the Lane
     * @param leftNeighbor
     * @param rightNeighbor
     */
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

    /**
     * Calculates whether the Lane is enterable based on the Surface's properties
     * @return whether the Lane is enterable
     */
    public boolean enterable() {
        Skeleton.printFunctionCall("Lane.enterable");
        Skeleton.printReturn();
        return surface.enterable();
    }

    /**
     * Tries to progress a CivilVehicle along the Lane according to Surface conditions,
     * and tells the CivilVehicle to set it's location to the Junction at the end if
     * it reached the end of Lane
     * @param cv the CivilVehicle to progress
     */
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

    /**
     * Tries to progress a Snowplow along the Lane and tells the Snowplow
     * if it reached the end of Lane
     * @param sn the Snowplow to progress
     */
    public void progress(Snowplow sn) {
        Skeleton.printFunctionCall("Lane.progress");
        int prog = surface.calculateProgress(sn);
        boolean ending = Skeleton.askBool("Did the snowplow reach the end of the lane?");
        if (ending) {
            sn.laneCleared(this, end);
        }
        Skeleton.printReturn();
    }

    /**
     * Returns the list of pushable Vehicles by checking surface conditions and
     * vehicle properties
     * @return the list of pushable Vehicles
     */
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

    /**
     * @param cv the CivilVehicle to check the closest Vehicle to
     * @return the closest Vehicle to the given CivilVehicle
     */
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

    /**
     * Closes the Lane as a result of a crash
     */
    public void crashHappened() {
        Skeleton.printFunctionCall("Lane.crashHappened");
        Skeleton.printReturn();
    }

    public int clearSnow() {
        Skeleton.printFunctionCall("Lane.clearSnow");
        Skeleton.printReturn();
        return surface.clearSnow();
    }

    public int clearIce() {
        Skeleton.printFunctionCall("Lane.clearIce");
        Skeleton.printReturn();
        return surface.clearIce();
    }

    public void salt() {
        Skeleton.printFunctionCall("Lane.salt");
        surface.setSalted();
        Skeleton.printReturn();
    }

    public void addSnow(int amount) {
        Skeleton.printFunctionCall("Lane.addSnow");
        surface.addSnow(amount);
        Skeleton.printReturn();
    }
}
