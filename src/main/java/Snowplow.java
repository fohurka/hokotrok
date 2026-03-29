import java.util.List;

public class Snowplow extends Vehicle {
    private Equipment eq;

    public Snowplow()
    {
        Skeleton.printFunctionCall("Snowplow.constructor");
        Skeleton.printReturn();
    }

    /**
     * Sets the equipment of the snowplow
     * @param eq the Equipment to equip
     */
    public void setEquipment(Equipment eq) {
        this.eq = eq;
    }

    /**
     * Tries to progress at the current location
     */
    @Override
    public void tick() {
        Skeleton.printFunctionCall("Snowplow.tick");
        getLocation().progress(this);
        Skeleton.printReturn();
    }

    /**
     * @return whether this vehicle is pushable
     */
    @Override
    public boolean pushable() {
        return false;
    }

    /**
     * Clears the current Lane and pushes all pushable vehicles in the current Lane and itself to the Junction
     * @param lane the current Lane
     * @param end the Junction at the end of the Lane
     */
    public void laneCleared(Lane lane, MapComponent end) {
        Skeleton.printFunctionCall("Snowplow.laneCleared");
        List<Vehicle> pushables = lane.getPushableCars();
        for (Vehicle v : pushables) {
            v.setLocation(end);
        }
        eq.use(lane);
        setLocation(end);
        Skeleton.printReturn();
    }

    /**
     * Responsible for acting upon a crash
     * Does nothing.
     */
    public void crash() {
    }

    public Equipment getCurrentEquipment() {
        Skeleton.printFunctionCall("Snowplow.getCurrentEquipment");
        Skeleton.printReturn();
        return eq;
    }
}
