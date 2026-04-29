public class DeepSnow extends Surface {
    
    public DeepSnow(Lane lane)
    {
        super(lane);
    }
    
    public DeepSnow(Lane lane, Modifier mod) {
        super(lane, mod);
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
        if (Skeleton.askBool("Van hó a felületen?"))
            addIce(1);
        Skeleton.printReturn();
    }
}
