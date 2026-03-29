public class Bus extends CivilVehicle {
    private BusPlayer owner;
    /**
     * Gets the player who owns and controls this vehicle.
     *
     * @return The owner Player (e.g., BusPlayer).
     */
    public BusPlayer getOwner() {
        Skeleton.printFunctionCall("Bus.getOwner");
        Skeleton.printReturn();
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
    public void tick() {
        Skeleton.printFunctionCall("Bus.tick");
        if (Skeleton.askBool("A busz ütközött állapotban van?"))
        {
            if (Skeleton.askBool("Sikerül a busznak elindulnia?"))
            {
                loc.crashRecovered();
            }   
        }
        else
        {
            getLocation().progress(this);
        }
        Skeleton.printReturn();
    }

    /**
     * @return whether this vehicle is pushable
     */
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
        Skeleton.printFunctionCall("Bus.stuckInCurrentLane");
        Skeleton.printReturn();
    }

    /**
     * Slips the vehicle and searches the Lane for another Vehicle to crash into
     * @param lane the current Lane
     */
    @Override
    public void slip(Lane lane) {
        Skeleton.printFunctionCall("Bus.slip");
        Vehicle nearest = lane.getNearest(this);
        if (nearest != null) {
            nearest.crash();
            lane.crashHappened();
        }
        Skeleton.printReturn();
    }

    /**
     * Crashes the vehicle
     */
    @Override
    public void crash() {
        Skeleton.printFunctionCall("Bus.crash");
        Skeleton.printReturn();
    }
}
