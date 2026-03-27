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

    public int clearSnow() {
        Skeleton.printFunctionCall("Surface.clearSnow");
        Skeleton.printReturn();
        return Skeleton.askInt("Surface.clearSnow return value: ");
    }
    
    public int clearIce() {
        Skeleton.printFunctionCall("Surface.clearIce");
        Skeleton.printReturn();
        return Skeleton.askInt("Surface.clearIce return value: ");
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
