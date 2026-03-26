public class Ice extends Surface {
    public Ice(Lane lane) {
        super(lane);
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
}
