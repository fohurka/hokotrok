public class SmallSnow extends Surface {

    /**
     * Constructs a SmallSnow surface for a given lane.
     *
     * @param lane The lane this surface belongs to.
     */
    public SmallSnow(Lane lane)
    {
        super(lane);
    }

    /**
     * Constructs a SmallSnow surface for a given lane with a specific modifier.
     *
     * @param lane The lane this surface belongs to.
     * @param mod  The surface modifier to apply.
     */
    public SmallSnow(Lane lane, Modifier mod) {
        super(lane, mod);
    }

    /**
     * Adds a specified amount of snow to the surface. If the snow amount exceeds
     * the threshold, the surface transforms into DeepSnow.
     *
     * @param amount The amount of snow to add.
     */
    @Override
    public void addSnow(int amount) {
        snowAmount += amount;
        if (snowAmount > snowThreshold) {
            Surface newSurf = new DeepSnow(lane, modifier);
            newSurf.addSnow(snowAmount);
            newSurf.addIce(iceAmount);
            lane.setSurface(newSurf);
        }
    }

    /**
     * Adds a specified amount of ice to the surface. If the ice amount exceeds
     * the threshold, the surface transforms into Ice.
     *
     * @param amount The amount of ice to add.
     */
    @Override
    public void addIce(int amount) {
        iceAmount += amount;
        if (iceAmount > iceThreshold) {
            Surface newSurf = new Ice(lane, modifier);
            newSurf.addIce(iceAmount);
            lane.setSurface(newSurf);
        }
    }

    /**
     * Removes all snow from the surface.
     *
     * @return The amount of snow removed.
     */
    @Override
    public int removeSnow() {
        int amount = snowAmount;
        snowAmount = 0;
        return amount;
    }

    /**
     * Removes up to a specified amount of snow from the surface.
     *
     * @param amount The maximum amount of snow to remove.
     * @return The actual amount of snow removed.
     */
    @Override
    public int removeSnow(int amount) {
        if (amount > snowAmount) {
            amount = snowAmount;
        }
        snowAmount -= amount;
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
     * Removes up to a specified amount of ice from the surface.
     *
     * @param amount The maximum amount of ice to remove.
     * @return The actual amount of ice removed.
     */
    @Override
    public int removeIce(int amount) {
        if (amount > iceAmount) {
            amount = iceAmount;
        }
        iceAmount -= amount;
        return amount;
    }

    /**
     * Calculates the progress of a CivilVehicle on this surface.
     *
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        return 1;
    }

    /**
     * Checks if the surface allows vehicles to enter the lane.
     *
     * @return true if the lane is enterable, false otherwise.
     */
    @Override
    public boolean enterable() {
        return true;
    }

    /**
     * Performs time-step updates, including applying weather effects from the modifier.
     */
    @Override
    public void tick() {
        modifier.applyWeather(this);
    }

    /**
     * Handles the event of a car passing over the surface, potentially adding ice if snow is present.
     */
    @Override
    protected void carPassed() {
        if (snowAmount > 0)
            addIce(1);
    }
}
