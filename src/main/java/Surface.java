public abstract class Surface {
    protected Lane lane;
    protected Modifier modifier;

    protected Lane getLane() {
        return lane;
    }

    public Surface(Lane lane) {
        this.lane = lane;
        modifier = new Unmodified();
    }

    /**
     * Calculates the movement progress for a Snowplow on this specific surface.
     * The calculation typically considers the current snow/ice levels and any active modifiers.
     * * @param sn The Snowplow attempting to move across the surface.
     * @return An integer representing the distance or progress units the vehicle can cover.
     */
    public int calculateProgress(Snowplow sn) {
        Skeleton.printFunctionCall("Surface.calculateProgress");
        Skeleton.printReturn();
        return 1;
    }

    /**
     * Removes an amount of snow 
     * @param amount the amount of snow
     */
    public void removeSnow(int amount) {
        Skeleton.printFunctionCall("Surface.removeSnow");
        Skeleton.printReturn();
    }

    /**
     * Removes an amount of ice 
     * @param amount the amount of ice
     */
    public void removeIce(int amount) {
        Skeleton.printFunctionCall("Surface.removeIce");
        Skeleton.printReturn();
    }

    /**
     * Fully clears the snow from this surface.
     * This is usually triggered by specialized equipment like an Impeller or DragonBlade.
     * * @return The total amount of snow that was removed during the operation.
     */
    public int clearSnow() {
        Skeleton.printFunctionCall("Surface.clearSnow");
        Skeleton.printReturn();
        return 1;
    }

    /**
     * Fully clears the ice from this surface.
     * This is usually triggered by specialized equipment like a DragonBlade.
     * * @return The total amount of ice that was removed during the operation.
     */
    public int clearIce() {
        Skeleton.printFunctionCall("Surface.clearIce");
        Skeleton.printReturn();
        return 1;
    }

    /**
     * Adds an amount of snow 
     * @param amount the amount of snow
     */
    public void addSnow(int amount) {
        Skeleton.printFunctionCall("Surface.addSnow");
        Skeleton.printReturn();
    }

    /**
     * Adds an amount of ice 
     * @param amount the amount of ice
     */
    public void addIce(int amount)
    {
        Skeleton.printFunctionCall("Surface.addIce");
        Skeleton.printReturn();
    }

    /**
     * Changes the active Modifier to Salted
     */
    public void salt() {
        Skeleton.printFunctionCall("Surface.salt");
        modifier = new Salted();
        Skeleton.printReturn();
    }

    protected abstract void carPassed();
    public abstract void tick();


    public abstract int calculateProgress(CivilVehicle cv);
    public abstract boolean enterable();
}
