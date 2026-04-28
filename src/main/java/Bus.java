public class Bus extends CivilVehicle {
    private BusPlayer owner;
    private boolean crashed = false;
    /**
     * Gets the player who owns and controls this vehicle.
     *
     * @return The owner Player (e.g., BusPlayer).
     */
    public BusPlayer getOwner() {
        return owner;
    }

    /**
     * Sets the player who owns this vehicle.
     *
     * @param owner The player to be set as the owner.
     */
    public void setOwner(BusPlayer owner) {
        this.owner = owner;
    }

    /**
     * Tries to progress at the current location or recover itself
     */
    @Override
    public void tick() {
        if (crashed) {
            crashed = false;
            if (loc != null) {
                loc.crashRecovered();
            }
        } else {
            if (getLocation() != null) {
                getLocation().progress(this);
            }
        }
    }

    /**
     * @return whether this vehicle is pushable
     */
    @Override
    public boolean pushable() {
        return false;
    }

    /**
     * Handles the logic when the Bus is stuck in the current Lane
     * Does nothing.
     * @param lane the current Lane
     */
    @Override
    public void stuckInCurrentLane(Lane lane) {
    }

    /**
     * Slips the vehicle and searches the Lane for another Vehicle to crash into
     * @param lane the current Lane
     */
    @Override
    public void slip(Lane lane) {
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            lane.crashHappened();
        }
    }

    /**
     * Crashes the vehicle
     */
    @Override
    public void crash() {
        crashed = true;
    }
}
