public class SmallSnow extends Surface {

    public SmallSnow(Lane lane)
    {
        super(lane);
    }

    public SmallSnow(Lane lane, Modifier mod) {
        super(lane);
        Skeleton.printFunctionCall("SmallSnow.constructor");
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
        Skeleton.printFunctionCall("SmallSnow.calculateProgress");
        Skeleton.printReturn();
        return 1;
    }

    /**
     * @return whether the surface makes the Lane enterable
     */
    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("SmallSnow.enterable");
        Skeleton.printReturn();
        return true;
    }

    
    /**
     * Applies its Modifer and tries to set the new surface to DeepSnow
     */
    @Override
    public void tick() {
        Skeleton.printFunctionCall("SmallSnow.tick");
        modifier.applyWeather(this);
        if (Skeleton.askBool("Does the snow amount get above the threshold of the DeepSnow state?"))
            lane.setSurface(new DeepSnow(lane, modifier));
        Skeleton.printReturn();
    }

    /**
     * Tries to add ice and tries to set the new surface to Ice
     */
    @Override
    protected void carPassed() {
        Skeleton.printFunctionCall("SmallSnow.carPassed");
        if (Skeleton.askBool("Is there any snow on the lane?"))
        {
            addIce(1);
            if (Skeleton.askBool("Does the ice amount get above the threshold of the Ice state?"))
                lane.setSurface(new Ice(lane, modifier));
        }
        Skeleton.printReturn();
    }
}
