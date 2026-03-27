public class Ice extends Surface {

    public Ice(Lane lane) {
        super(lane);
    }

    public Ice(Lane lane, Modifier mod) {
        Skeleton.printFunctionCall("Ice.constructor");
        super(lane);
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
        Skeleton.printFunctionCall("Ice.calculateProgress");
        boolean slip = Skeleton.askBool("Does the vehicle slip?");
        if (slip) {
            cv.slip(getLane());
            Skeleton.printReturn();
            return 0;
        }
        Skeleton.printReturn();
        return 1;
    }

    /**
     * @return whether the surface makes the Lane enterable
     */
    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("Ice.enterable");
        Skeleton.printReturn();
        return true;
    }

    /**
     * Applies its Modifer and tries to set the new surface to SmallSnow
     */
    @Override
    public void tick() {
        Skeleton.printFunctionCall("Ice.tick");
        modifier.applyWeather(this);

        if (Skeleton.askBool("Does the ice amount get below the threshold of the Ice state ?"))
            lane.setSurface(new SmallSnow(lane, modifier));
        Skeleton.printReturn();
    }

    /**
     * Tries to add ice
     */
    @Override
    protected void carPassed() {
        Skeleton.printFunctionCall("Ice.carPassed");
        if (Skeleton.askBool("Is there any snow on the lane?"))
            addIce(1);
        Skeleton.printReturn();
    }
}
