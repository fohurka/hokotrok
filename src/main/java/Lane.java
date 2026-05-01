import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

public class Lane extends MapComponent {
    private Surface surface = null;
    private final Junction start;
    private final Junction end;
    private Lane rightNeighbor;
    private Lane leftNeighbor;
    private RoadNetwork rn;
    private int length;
    private final HashMap<Vehicle, Integer> progress = new HashMap<>();
    private boolean isCrashed = false;

    public Lane(Junction start, Junction end, int length) {
        super();
        this.length = length;
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
    public void cleared(SnowplowPlayer p, int amount) {
        if (rn != null) {
            rn.laneCleared(p, amount);
        }
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
        return leftNeighbor;
    }

    public Lane getRightNeighbor() {
        return rightNeighbor;
    }

    /**
     * Calculates whether the Lane is enterable based on the Surface's properties
     * @return whether the Lane is enterable
     */
    public boolean enterable() {
        return surface.enterable();
    }

    /**
     * Tries to progress a CivilVehicle along the Lane according to Surface conditions,
     * and tells the CivilVehicle to set it's location to the Junction at the end if
     * it reached the end of Lane
     * @param cv the CivilVehicle to progress
     */
    public void progress(CivilVehicle cv) {
        if (surface == null || isCrashed) return;
        int prog = surface.calculateProgress(cv);

        progress.put(cv, progress.get(cv) + prog);

        boolean ending = progress.get(cv) >= length;

        if (ending) {
            cv.setLocation(end);
        }
    }

    /**
     * Tries to progress a Snowplow along the Lane and tells the Snowplow
     * if it reached the end of Lane
     * @param sn the Snowplow to progress
     */
    public void progress(Snowplow sn) {
        if (surface == null) return;
        int prog = surface.calculateProgress(sn);

        progress.put(sn, progress.get(sn) + prog);

        boolean ending = progress.get(sn) >= length;

        if (ending) {
            sn.laneCleared(this, end);
        }
    }

    /**
     * Returns the list of pushable Vehicles by checking surface conditions and
     * vehicle properties
     * @return the list of pushable Vehicles
     */
    public List<Vehicle> getPushableCars() {
        List<Vehicle> pushables = new ArrayList<>();

        if (surface.enterable()) {
            return pushables;
        }

        for (Vehicle v : getVehicles()) {
            if (v.pushable()) {
                pushables.add(v);
            }
        }

        return pushables;
    }

    /**
     * @param cv the CivilVehicle to check the closest Vehicle to
     * @return the closest Vehicle to the given CivilVehicle
     */
    public Vehicle getNearest(CivilVehicle cv) {
        int minDist = Integer.MAX_VALUE;
        Vehicle nearest = null;

        for (Vehicle v : getVehicles()) {
            if (v != cv && abs(progress.get(v) - progress.get(cv)) < minDist) {
                nearest = v;
                minDist = abs(progress.get(v) - progress.get(cv));
            }
        }
        return nearest;
    }

    /**
     * Closes the Lane as a result of a crash
     */
    public void crashHappened() {
        isCrashed = true;
    }

    public int clearSnow() {
        return surface.removeSnow();
    }

    public int clearIce() {
        return surface.removeIce();
    }

    /**
     * Changes its Surfaces modifier to Salted indirectly
     */
    public void salt() {
        surface.salt();
    }

    /**
     * Notifies the Surface of a tick of time passing
     */
    public void tick() {
        surface.tick();
    }

    /**
     * Removes a Vehicle from the Lane and notifies the Surface of a Vehicle passing through
     * @param vehicle
     */
    @Override
    public void remove(Vehicle vehicle)
    {
        progress.remove(vehicle);
        vehicles.remove(vehicle);
        surface.carPassed();
    }

    @Override
    public void arrived(Vehicle vehicle) {
        progress.put(vehicle, 0);
        vehicles.add(vehicle);
    }

    /**
     * If the lane was blocked from a crash it is set back to normal functionality
     */
    public void crashRecovered()
    {
        isCrashed = false;
        //TODO: iterate over vehicles to check if there are still crashed ones remaining instead of blindly setting isCrashed to false
    }

    /**
     * Adds the specified amount of snow to the surface by calling surface.addSnow
     * @param amount the amount of snow to be added
     */
    public void addSnow(int amount) {
        surface.addSnow(amount);
    }

    public void setRightNeighbor(Lane l) {
        this.rightNeighbor = l;
    }
    
    public void setLeftNeighbor(Lane l) {
        this.leftNeighbor = l;
    }

    /**
     * Applies grit to the surface of the lane by calling surface.grit
     */
    public void grit() {
        surface.grit();
    }

    /**
     * Clears the grit from the lane by calling removeGrit on the surface
     */
    public void clearGrit() {
        surface.removeGrit();
    }

    /**
     * Getter for length
     * @return the length of the lane
     */
    public int getLength() {
        return length;
    }

    public Junction getStart() { return start; }
    public Junction getEnd() { return end; }
    public Surface getSurface() { return surface; }
    public HashMap<Vehicle, Integer> getProgress() { return progress; }
}
