public class Bus extends CivilVehicle {

    /**
     * Tries to progress at the current location
     */
    public void tick() {
        Skeleton.printFunctionCall("Bus.tick");
        getLocation().progress(this);
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
