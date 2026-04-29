import java.util.Random;

public class Ice extends Surface {

    public Ice(Lane lane) {
        super(lane);
    }

    public Ice(Lane lane, Modifier mod) {
        super(lane);
        modifier = mod;
    }

    @Override
    public void addSnow(int amount) { } // can't snow on an icy surface

    @Override
    public void addIce(int amount) {
        iceAmount += amount;
    }

    @Override
    public int removeSnow() {
        int amount = snowAmount;
        snowAmount = 0;
        return amount;
    }

    @Override
    public int removeSnow(int amount) {
        if (amount > snowAmount) {
            amount = snowAmount;
        }
        snowAmount -= amount;
        return amount;
    }

    @Override
    public int removeIce() {
        int amount = iceAmount;
        iceAmount = 0;
        Surface newSurf = new SmallSnow(lane, modifier);
        newSurf.addSnow(iceAmount); // ice gets converted to snow after breaking it with icebreaker
        lane.setSurface(newSurf);
        return amount;
    }

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
     * Calculates the progress of a CivilVehicle
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        Random rand = new Random();
        boolean slip = rand.nextInt(100) < 15; //15% chance to slip
        if (slip) {
            cv.slip(lane);
            return 0;
        }
        return 1;
    }

    /**
     * @return whether the surface makes the Lane enterable
     */
    @Override
    public boolean enterable() {
        return true;
    }

    /**
     * Applies its Modifer and tries to set the new surface to SmallSnow
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
        if (snowAmount > 0)
            addIce(1);
    }
}
