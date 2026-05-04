public class Grit extends Surface {
    /**
     * Constructs a new Grit surface on the specified lane.
     *
     * @param lane the lane this surface belongs to
     */
    public Grit(Lane lane) {
        super(lane);
    }

    /**
     * Constructs a new Grit surface on the specified lane with a given modifier.
     *
     * @param lane     the lane this surface belongs to
     * @param modifier the modifier to apply to this surface
     */
    public Grit(Lane lane, Modifier modifier) {
        super(lane, modifier);
    }

    /**
     * Adds snow to the grit surface. If the snow amount reaches the threshold,
     * the grit is removed and the surface transitions.
     *
     * @param amount the amount of snow to add
     */
    @Override
    public void addSnow(int amount) {
        snowAmount += amount;
        if (snowAmount >= snowThreshold) {
            removeGrit();
        }
    }

    /**
     * Adds ice to the grit surface. If the ice amount reaches the threshold,
     * the surface transitions to Ice.
     *
     * @param amount the amount of ice to add
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
     * Removes all snow from the surface.
     *
     * @return the amount of snow removed
     */
    @Override
    public int removeSnow() {
        int amount = snowAmount;
        snowAmount = 0;
        return amount;
    }

    /**
     * Removes a specified amount of snow from the surface.
     *
     * @param amount the amount of snow to remove
     * @return the actual amount of snow removed
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
     * @return the amount of ice removed
     */
    @Override
    public int removeIce() {
        int amount = iceAmount;
        iceAmount = 0;
        return amount;
    }

    /**
     * Removes a specified amount of ice from the surface.
     *
     * @param amount the amount of ice to remove
     * @return the actual amount of ice removed
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
     * Calculates the progress of a civil vehicle on this surface.
     *
     * @param cv the civil vehicle
     * @return the progress amount
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        return 1;
    }

    /**
     * Checks if the surface is enterable by vehicles.
     *
     * @return true if enterable, false otherwise
     */
    @Override
    public boolean enterable() {
        return true;
    }

    /**
     * Applies weather effects to the surface.
     */
    @Override
    public void tick() {
        modifier.applyWeather(this);
    }

    /**
     * Called when a car passes over the surface.
     */
    @Override
    public void carPassed() { }

    /**
     * Removes grit from the surface, transitioning it to a SmallSnow surface.
     *
     * @return true if grit was removed
     */
    @Override
    public boolean removeGrit() {
        Surface newSurf = new SmallSnow(lane, modifier);
        lane.setSurface(newSurf);
        newSurf.addSnow(snowAmount);
        return true;
    }
}
