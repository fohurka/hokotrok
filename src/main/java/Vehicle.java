public abstract class Vehicle {
    protected String id;
    protected MapComponent loc;

    protected boolean isCrashed;

    /**
     * Triggers the crash state for this vehicle.
     */
    public abstract void crash();

    /**
     * Advances the vehicle's state by one time step.
     */
    public abstract void tick();

    /**
     * Determines if the vehicle can be pushed by other vehicles or equipment.
     *
     * @return true if pushable, false otherwise
     */
    public abstract boolean pushable();

    /**
     * Updates the vehicle's position on the map.
     * This method handles the logic of leaving the current component (if any)
     * and arriving at the destination component, maintaining the integrity
     * of the map's vehicle tracking.
     *
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
     *
     * @return The MapComponent (e.g., Lane or Junction) currently holding this vehicle.
     */
    public MapComponent getLocation() {
        return loc;
    }

    /**
     * Returns the unique identifier of the vehicle.
     *
     * @return the vehicle id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this vehicle.
     *
     * @param id the new vehicle id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks if the vehicle is currently in a crashed state.
     *
     * @return true if crashed, false otherwise
     */
    public boolean isCrashed() {
        return isCrashed;
    }
}