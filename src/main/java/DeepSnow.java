public class DeepSnow extends Surface {
    
    public DeepSnow(Lane lane)
    {
        super(lane);
    }
    
    public DeepSnow(Lane lane, Modifier mod) {
        super(lane);
        Skeleton.printFunctionCall("DeepSnow.constructor");
        modifier = mod;
        Skeleton.printReturn();
    }
    
    /**
     * Calculates the progress of a CivilVehicle
     * @param cv the CivilVehicle that progresses
     * @return the amount of progress the CivilVehicle made
     */
    @Override
    public int calculateProgress(CivilVehicle cv) {
        Skeleton.printFunctionCall("DeepSnow.calculateProgress");
        cv.stuckInCurrentLane(getLane());
        Skeleton.printReturn();
        return 0;
    }

    /**
     * @return whether the surface makes the Lane enterable
     */
    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("DeepSnow.enterable");
        Skeleton.printReturn();
        return false;
    }

    /**
     * Applies its Modifer and tries to set the new surface to SmallSnow
     */
    @Override
    public void tick() {
        Skeleton.printFunctionCall("DeepSnow.tick");
        modifier.applyWeather(this);

        if (Skeleton.askBool("A hó mennyisége a vastag hó mértéke alá csökken?"))
            lane.setSurface(new SmallSnow(lane, modifier));
        Skeleton.printReturn();
    }

    /**
     * Tries to add ice
     */
    @Override
    protected void carPassed() {
        Skeleton.printFunctionCall("DeepSnow.carPassed");
        addIce(1);
        Skeleton.printReturn();
    }
}
