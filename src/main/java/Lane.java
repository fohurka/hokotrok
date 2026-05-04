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

    /**
     * Checks if the lane is currently blocked due to a crash.
     *
     * @return true if the lane is crashed, false otherwise.
     */
    public boolean isCrashed() {
        return isCrashed;
    }

    /**
     * Constructs a new Lane connecting two junctions with a specified length.
     *
     * @param start  The starting junction of the lane.
     * @param end    The ending junction of the lane.
     * @param length The length of the lane.
     */
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
     * @param amount The amount of snow cleared.
     */
    public void cleared(SnowplowPlayer p, int amount) {
        if (rn != null) {
            rn.laneCleared(p, amount);
        }
    }
    /**
     * Sets the neighbors of the Lane
     * @param leftNeighbor the lane to the left
     * @param rightNeighbor the lane to the right
     */
    public void addNeighbors(Lane leftNeighbor, Lane rightNeighbor) {
        this.leftNeighbor = leftNeighbor;
        this.rightNeighbor = rightNeighbor;
    }

    /**
     * Sets the progress of a vehicle on this lane.
     * @param v the vehicle
     * @param p the progress value
     */
    public void setProgress(Vehicle v, int p) {
        progress.put(v, p);
    }

    /**
     * Returns the left neighbor of this lane.
     *
     * @return The left neighbor lane, or null if none.
     */
    public Lane getLeftNeighbor() {
        return leftNeighbor;
    }

    /**
     * Returns the right neighbor of this lane.
     *
     * @return The right neighbor lane, or null if none.
     */
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

        if (!progress.containsKey(cv)) { return; }
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

        Integer currentProg = progress.get(sn);
        if (currentProg == null) {
            currentProg = 0;
        }
        progress.put(sn, currentProg + prog);

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
     * Returns the nearest vehicle to the given civil vehicle on this lane.
     *
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

    /**
     * Removes snow from the surface of the lane.
     *
     * @return The amount of snow removed.
     */
    public int clearSnow() {
        return surface.removeSnow();
    }

    /**
     * Removes ice from the surface of the lane.
     *
     * @return The amount of ice removed.
     */
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
     * @param vehicle the vehicle to remove
     */
    @Override
    public void remove(Vehicle vehicle)
    {
        progress.remove(vehicle);
        vehicles.remove(vehicle);
        surface.carPassed();
    }

    /**
     * Notifies the lane that a vehicle has arrived.
     *
     * @param vehicle The vehicle that arrived.
     */
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
        for (Vehicle v : getVehicles()) {
            if (v.isCrashed()) {
                isCrashed = true;
                break;
            }
        }
    }

    /**
     * Adds the specified amount of snow to the surface by calling surface.addSnow
     * @param amount the amount of snow to be added
     */
    public void addSnow(int amount) {
        surface.addSnow(amount);
    }

    /**
     * Sets the right neighbor of this lane.
     *
     * @param l The lane to set as the right neighbor.
     */
    public void setRightNeighbor(Lane l) {
        this.rightNeighbor = l;
    }
    
    /**
     * Sets the left neighbor of this lane.
     *
     * @param l The lane to set as the left neighbor.
     */
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
     *
     * @return true if grit was removed, false otherwise.
     */
    public boolean clearGrit() {
        return surface.removeGrit();
    }

    /**
     * Getter for length
     * @return the length of the lane
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns the starting junction of the lane.
     *
     * @return The starting junction.
     */
    public Junction getStart() { return start; }

    /**
     * Returns the ending junction of the lane.
     *
     * @return The ending junction.
     */
    public Junction getEnd() { return end; }

    /**
     * Returns the surface of the lane.
     *
     * @return The surface object.
     */
    public Surface getSurface() { return surface; }

    /**
     * Returns the progress map of vehicles on this lane.
     *
     * @return A map of vehicles to their progress positions.
     */
    public HashMap<Vehicle, Integer> getProgress() { return progress; }
}
