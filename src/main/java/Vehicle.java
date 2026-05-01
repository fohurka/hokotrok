public abstract class Vehicle {
    protected String id;
    protected MapComponent loc;

    public abstract void crash();

    public abstract void tick();

    public abstract boolean pushable();

    /**
     * Updates the vehicle's position on the map.
     * This method handles the logic of leaving the current component (if any)
     * and arriving at the destination component, maintaining the integrity
     * of the map's vehicle tracking.
     * @param dest The new MapComponent the vehicle is moving into.
     */
    public void setLocation(MapComponent dest) {
        if (loc != null) {
            loc.remove(this);
        }
        if (dest != null) {
            dest.arrived(this);
        }
        loc = dest;
    }

    /**
     * Retrieves the current map component where the vehicle is located.
     * @return The MapComponent (e.g., Lane or Junction) currently holding this vehicle.
     */
    public MapComponent getLocation() {
        return loc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}