import java.util.List;

public class Snowplow extends Vehicle {
    private Equipment eq;

    /**
     * Constructs a new Snowplow and initializes its crash state to false.
     */
    public Snowplow() {
        isCrashed = false;
    }

    /**
     * Sets the equipment mounted on the snowplow.
     *
     * @param eq the Equipment to equip.
     */
    public void setEquipment(Equipment eq) {
        this.eq = eq;
    }

    /**
     * Advances the snowplow's state by one time step, triggering its progress at its current location.
     */
    @Override
    public void tick() {
        getLocation().progress(this);
    }

    /**
     * Checks if this vehicle can be pushed (e.g., when a lane is blocked).
     *
     * @return false, as snowplows are not pushable.
     */
    @Override
    public boolean pushable() {
        return false;
    }

    /**
     * Handles the completion of clearing a lane. It pushes all pushable vehicles currently on the lane
     * to the destination junction, uses the mounted equipment on the lane, and moves the snowplow
     * to the destination junction.
     *
     * @param lane the current Lane being cleared.
     * @param end the Junction at the end of the Lane.
     */
    public void laneCleared(Lane lane, MapComponent end) {
        if (!lane.getSurface().enterable()) {
            List<Vehicle> pushables = lane.getPushableCars();
            for (Vehicle v : pushables) {
                v.setLocation(end);
            }
        }
        eq.use(lane);
        setLocation(end);
    }

    /**
     * Handles the event of a crash involving this snowplow.
     * Currently does nothing.
     */
    public void crash() {
    }

    /**
     * Returns the equipment currently mounted on the snowplow.
     *
     * @return the current Equipment, or null if none is equipped.
     */
    public Equipment getCurrentEquipment() {
        return eq;
    }

    /**
     * Returns the player who owns this snowplow, derived from its mounted equipment.
     *
     * @return the owning SnowplowPlayer, or null if no equipment is mounted.
     */
    public SnowplowPlayer getOwner() {
        return (eq != null) ? eq.owner : null;
    }
}
