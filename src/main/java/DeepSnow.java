public class DeepSnow extends Surface {
    
    public DeepSnow(Lane lane)
    {
        super(lane);
    }
    
    public DeepSnow(Lane lane, Modifier mod) {
        super(lane, mod);
    }

    @Override
    public void addSnow(int amount) {
        snowAmount += amount;
    }

    @Override
    public void addIce(int amount) {
        iceAmount += amount;
        if (iceAmount >= iceThreshold) {
            Surface newSurf = new Ice(lane, modifier);
            newSurf.addIce(iceAmount);
            lane.setSurface(newSurf);
        }
    }

    @Override
    public int removeSnow() {
        int amount = snowAmount;
        snowAmount = 0;
        Surface newSurf = new SmallSnow(lane, modifier);
        lane.setSurface(newSurf);
        return amount;
    }

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

    @Override
    public int removeIce() {
        int amount = iceAmount;
        iceAmount = 0;
        return amount;
    }

    @Override
    public int removeIce(int amount) {
        if (iceAmount >= iceThreshold) {
            amount = iceAmount;
        }
        iceAmount -= amount;
        return amount;
    }
    
    /**
     * Calculates the progress of a CivilVehicle
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        cv.stuckInCurrentLane(lane);
        return 0;
    }

    /**
     * @return whether the surface makes the Lane enterable
     */
    @Override
    public boolean enterable() {
        return false;
    }

    /**
     * Applies its Modifier and tries to set the new surface to SmallSnow
     */
    @Override
    public void tick() {
        modifier.applyWeather(this);
    }

    /**
     * Tries to add ice
     */
    @Override
    protected void carPassed() {
        addIce(1);
    }
}
