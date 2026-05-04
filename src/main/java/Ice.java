import java.util.Random;

public class Ice extends Surface {

    /**
     * Constructs a new Ice surface on the specified lane.
     *
     * @param lane the lane this surface belongs to
     */
    public Ice(Lane lane) {
        super(lane);
    }

    /**
     * Constructs a new Ice surface on the specified lane with a given modifier.
     *
     * @param lane the lane this surface belongs to
     * @param mod  the modifier to apply to this surface
     */
    public Ice(Lane lane, Modifier mod) {
        super(lane);
        modifier = mod;
    }

    /**
     * Snow cannot be added to an icy surface.
     *
     * @param amount the amount of snow to add
     */
    @Override
    public void addSnow(int amount) { } // can't snow on an icy surface

    /**
     * Adds ice to the surface.
     *
     * @param amount the amount of ice to add
     */
    @Override
    public void addIce(int amount) {
        iceAmount += amount;
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
     * Removes all ice from the surface, transitioning it to a SmallSnow surface.
     *
     * @return the amount of ice removed
     */
    @Override
    public int removeIce() {
        int amount = iceAmount;
        iceAmount = 0;
        Surface newSurf = new SmallSnow(lane, modifier);
        newSurf.addSnow(iceAmount); // ice gets converted to snow after breaking it with icebreaker
        lane.setSurface(newSurf);
        return amount;
    }

    /**
     * Removes a specified amount of ice from the surface. If the ice amount falls
     * below the threshold, the surface transitions to SmallSnow.
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
        if (iceAmount < iceThreshold) {
            Surface newSurf = new SmallSnow(lane, modifier);
            newSurf.addSnow(iceAmount);
            lane.setSurface(newSurf);
        }
        return amount;
    }

    /**
     * Calculates the progress of a CivilVehicle.
     * There is a chance for the vehicle to slip on the ice.
     *
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        Random rand = new Random();
        boolean slip = GameController.isRandom() ? rand.nextInt(100) < 15 : true; //15% chance to slip
        if (slip) {
            cv.slip(lane);
            return 0;
        }
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
     * Called when a car passes over the surface. If there is snow, it may turn to ice.
     */
    @Override
    protected void carPassed() {
        if (snowAmount > 0)
            addIce(1);
    }
}
