public class Grit extends Surface {
    public Grit(Lane lane) {
        super(lane);
    }

    public Grit(Lane lane, Modifier modifier) {
        super(lane, modifier);
    }

    @Override
    public void addSnow(int amount) {
        snowAmount += amount;
        if (snowAmount >= snowThreshold) {
            removeGrit();
        }
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
        return amount;
    }

    @Override
    public int removeIce(int amount) {
        if (amount > iceAmount) {
            amount = iceAmount;
        }
        iceAmount -= amount;
        return amount;
    }

    @Override
    public int calculateProgress(CivilVehicle cv) {
        return 1;
    }

    @Override
    public boolean enterable() {
        return true;
    }

    @Override
    public void tick() {
        modifier.applyWeather(this);
    }

    @Override
    public void carPassed() { }

    @Override
    public boolean removeGrit() {
        Surface newSurf = new SmallSnow(lane, modifier);
        lane.setSurface(newSurf);
        return true;
    }
}
