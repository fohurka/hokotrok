public abstract class Surface {
    protected Lane lane;
    protected Modifier modifier = new Unmodified();

    protected int snowAmount = 0;
    protected int iceAmount = 0;
    public static int snowThreshold = 4;
    public static int iceThreshold = 4;

    /**
     * Returns the lane associated with this surface.
     *
     * @return the lane instance
     */
    protected Lane getLane() {
        return lane;
    }

    /**
     * Constructs a Surface for the given lane with default Unmodified modifier.
     *
     * @param lane the lane this surface belongs to
     */
    public Surface(Lane lane) {
        this.lane = lane;
    }

    /**
     * Constructs a Surface for the given lane with a specified modifier.
     *
     * @param lane     the lane this surface belongs to
     * @param modifier the initial modifier for the surface
     */
    public Surface(Lane lane, Modifier modifier) {
        this.lane = lane;
        this.modifier = modifier;
    }

    /**
     * Calculates the movement progress for a Snowplow on this specific surface.
     * The calculation typically considers the current snow/ice levels and any
     * active modifiers.
     *
     * @param sn The Snowplow attempting to move across the surface.
     * @return An integer representing the distance or progress units the vehicle
     *         can cover.
     */
    public int calculateProgress(Snowplow sn) {
        return 1;
    }

    /**
     * Fully clears the snow from this surface.
     * This is usually triggered by specialized equipment like an Impeller or
     * DragonBlade.
     *
     * @return The total amount of snow that was removed during the operation.
     */
    public abstract int removeSnow();

    /**
     * Removes a specific amount of snow from this surface.
     *
     * @param amount the maximum amount of snow to remove
     * @return the actual amount of snow removed
     */
    public abstract int removeSnow(int amount);

    /**
     * Fully clears the ice from this surface.
     * This is usually triggered by specialized equipment like a DragonBlade.
     *
     * @return The total amount of ice that was removed during the operation.
     */
    public abstract int removeIce();

    /**
     * Removes a specific amount of ice from this surface.
     *
     * @param amount the maximum amount of ice to remove
     * @return the actual amount of ice removed
     */
    public abstract int removeIce(int amount);

    /**
     * Adds an amount of snow to this surface.
     *
     * @param amount the amount of snow to add
     */
    public abstract void addSnow(int amount);

    /**
     * Adds an amount of ice to this surface.
     *
     * @param amount the amount of ice to add
     */
    public abstract void addIce(int amount);

    /**
     * Returns the current amount of snow on this surface.
     *
     * @return the snow amount
     */
    public int getSnowAmount() {
        return snowAmount;
    }

    /**
     * Returns the current amount of ice on this surface.
     *
     * @return the ice amount
     */
    public int getIceAmount() {
        return iceAmount;
    }

    /**
     * Returns the active modifier on this surface.
     *
     * @return the modifier instance
     */
    public Modifier getModifier() {
        return modifier;
    }

    /**
     * Changes the active Modifier to Salted.
     */
    public void salt() {
        modifier = new Salted();
    }

    /**
     * Grits the surface, replacing it with a Grit surface while preserving snow and ice levels.
     */
    public void grit() {
        Surface newSurf = new Grit(lane, modifier);
        newSurf.iceAmount = iceAmount;
        newSurf.snowAmount = snowAmount;
        lane.setSurface(newSurf);
    }

    /**
     * Attempts to remove grit from the surface.
     *
     * @return true if grit was removed, false otherwise
     */
    public boolean removeGrit() {
        return false;
    }

    /**
     * Hook called when a car passes over this surface, potentially affecting its state.
     */
    protected abstract void carPassed();

    /**
     * Advances the surface state by one time step.
     */
    public abstract void tick();

    /**
     * Calculates the movement progress for a civil vehicle on this specific surface.
     *
     * @param cv the civil vehicle attempting to move
     * @return the progress units the vehicle can cover
     */
    public abstract int calculateProgress(CivilVehicle cv);

    /**
     * Determines if the surface is currently enterable by vehicles.
     *
     * @return true if enterable, false otherwise
     */
    public abstract boolean enterable();
}
