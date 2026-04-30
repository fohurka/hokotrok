public abstract class Surface {
    protected Lane lane;
    protected Modifier modifier = new Unmodified();

    protected int snowAmount = 0;
    protected int iceAmount = 0;
    protected static int snowThreshold = 4;
    protected static int iceThreshold = 4;

    protected Lane getLane() {
        return lane;
    }

    public Surface(Lane lane) {
        this.lane = lane;
    }

    public Surface(Lane lane, Modifier modifier) {
        this.lane = lane;
        this.modifier = modifier;
    }

    /**
     * Calculates the movement progress for a Snowplow on this specific surface.
     * The calculation typically considers the current snow/ice levels and any active modifiers.
     * * @param sn The Snowplow attempting to move across the surface.
     * @return An integer representing the distance or progress units the vehicle can cover.
     */
    public int calculateProgress(Snowplow sn) {
        return 1;
    }

    /**
     * Fully clears the snow from this surface.
     * This is usually triggered by specialized equipment like an Impeller or DragonBlade.
     * @return The total amount of snow that was removed during the operation.
     */
    public abstract int removeSnow();

    public abstract int removeSnow(int amount);

    /**
     * Fully clears the ice from this surface.
     * This is usually triggered by specialized equipment like a DragonBlade.
     * * @return The total amount of ice that was removed during the operation.
     */
    public abstract int removeIce();

    public abstract int removeIce(int amount);

    /**
     * Adds an amount of snow 
     * @param amount the amount of snow
     */
    public abstract void addSnow(int amount);

    /**
     * Adds an amount of ice 
     * @param amount the amount of ice
     */
    public abstract void addIce(int amount);

    public int getSnowAmount() { return snowAmount; }
    public int getIceAmount() { return iceAmount; }
    public Modifier getModifier() { return modifier; }

    /**
     * Changes the active Modifier to Salted
     */
    public void salt() {
        modifier = new Salted();
    }

    public void grit() {
        Surface newSurf = new Grit(lane, modifier);
        lane.setSurface(newSurf);
    }

    public void removeGrit() { }

    protected abstract void carPassed();
    public abstract void tick();


    public abstract int calculateProgress(CivilVehicle cv);
    public abstract boolean enterable();
}
