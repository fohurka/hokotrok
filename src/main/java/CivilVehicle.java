public abstract class CivilVehicle extends Vehicle {

    /**
     * Handles the logic when the vehicle is stuck in its current lane.
     *
     * @param lane The current lane where the vehicle is stuck.
     */
    public abstract void stuckInCurrentLane(Lane lane);

    /**
     * Handles the logic when the vehicle slips in its current lane.
     *
     * @param lane The current lane where the vehicle slipped.
     */
    public abstract void slip(Lane lane);

    /**
     * Crashes the vehicle.
     */
    public abstract void crash();

    /**
     * Progresses the vehicle's state for one time step.
     */
    public abstract void tick();

    /**
     * Indicates whether the vehicle can be pushed.
     *
     * @return true if the vehicle is pushable, false otherwise.
     */
    public abstract boolean pushable();
}