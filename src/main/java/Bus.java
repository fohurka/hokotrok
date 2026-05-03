import java.util.List;

public class Bus extends CivilVehicle {
    private BusPlayer owner;
    private List<Building> stations;
    private int timer = 0;
    /**
     * Gets the player who owns and controls this vehicle.
     *
     * @return The owner Player (e.g., BusPlayer).
     */
    public BusPlayer getOwner() {
        return owner;
    }

    public List<Building> getStations() {
        return stations;
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
        if (isCrashed) {
            if (timer > 3) {
                isCrashed = false;
                if (loc != null) {
                    loc.crashRecovered();
                }
            }
            else
                timer++;
        } else {
            if (getLocation() != null) {
                getLocation().progress(this);
                if(getLocation() instanceof Junction) {
                    Junction j = (Junction) getLocation();
                    j.arrived(this);
                    if (stations != null && stations.size() >= 2 && j.getBuildings().contains(stations.get(1))) {
                        Building tmp = stations.get(0);
                        stations.set(0, stations.get(1));
                        stations.set(1, tmp);
                    }
                }
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
            isCrashed = true;
            nearest.crash();
            lane.crashHappened();
        }
    }

    public boolean isCrashed() {
        return isCrashed;
    }
    public void setStations(List<Building> stations){
        this.stations = stations;
    }

    /**
     * Crashes the vehicle
     */
    @Override
    public void crash() {
        isCrashed = true;
        timer = 0;
    }
}
