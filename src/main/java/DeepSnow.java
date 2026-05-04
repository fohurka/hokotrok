public class DeepSnow extends Surface {
    
    /**
     * Constructs a new DeepSnow surface for the specified lane.
     *
     * @param lane The lane where this surface is located.
     */
    public DeepSnow(Lane lane)
    {
        super(lane);
    }
    
    /**
     * Constructs a new DeepSnow surface for the specified lane with a modifier.
     *
     * @param lane The lane where this surface is located.
     * @param mod  The modifier to be applied to this surface.
     */
    public DeepSnow(Lane lane, Modifier mod) {
        super(lane, mod);
    }

    /**
     * Adds snow to the surface.
     *
     * @param amount The amount of snow to add.
     */
    @Override
    public void addSnow(int amount) {
        snowAmount += amount;
    }

    /**
     * Adds ice to the surface. If the ice amount reaches the threshold,
     * the surface transforms into Ice.
     *
     * @param amount The amount of ice to add.
     */
    @Override
    public void addIce(int amount) {
        iceAmount += amount;
        if (iceAmount >= iceThreshold) {
            Surface newSurf = new Ice(lane, modifier);
            newSurf.addIce(iceAmount);
            lane.setSurface(newSurf);
        }
    }

    /**
     * Removes all snow from the surface and transforms it into SmallSnow.
     *
     * @return The amount of snow removed.
     */
    @Override
    public int removeSnow() {
        int amount = snowAmount;
        snowAmount = 0;
        Surface newSurf = new SmallSnow(lane, modifier);
        lane.setSurface(newSurf);
        return amount;
    }

    /**
     * Removes a specified amount of snow. If the remaining snow is below the threshold,
     * it transforms into SmallSnow.
     *
     * @param amount The amount of snow to remove.
     * @return The actual amount of snow removed.
     */
    @Override
    public int removeSnow(int amount) {
        if (amount > snowAmount) {
            amount = snowAmount;
        }
        snowAmount -= amount;
        if (snowAmount < snowThreshold) {
            Surface newSurf = new SmallSnow(lane, modifier);
            newSurf.addSnow(amount);
            lane.setSurface(newSurf);
        }
        return amount;
    }

    /**
     * Removes all ice from the surface.
     *
     * @return The amount of ice removed.
     */
    @Override
    public int removeIce() {
        int amount = iceAmount;
        iceAmount = 0;
        return amount;
    }

    /**
     * Removes a specified amount of ice.
     *
     * @param amount The amount of ice to remove.
     * @return The actual amount of ice removed.
     */
    @Override
    public int removeIce(int amount) {
        if (iceAmount >= iceThreshold) {
            amount = iceAmount;
        }
        iceAmount -= amount;
        return amount;
    }
    
    /**
     * Calculates the progress of a CivilVehicle on this deep snow surface.
     * The vehicle gets stuck.
     *
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made (always 0)
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        cv.stuckInCurrentLane(lane);
        return 0;
    }

    /**
     * Determines if the surface makes the Lane enterable.
     *
     * @return false, as deep snow is not enterable.
     */
    @Override
    public boolean enterable() {
        return false;
    }

    /**
     * Updates the surface state for one time step by applying the weather modifier.
     */
    @Override
    public void tick() {
        modifier.applyWeather(this);
    }

    /**
     * Handles logic when a car passes over the surface. Adds a small amount of ice.
     */
    @Override
    protected void carPassed() {
        addIce(1);
    }
}
