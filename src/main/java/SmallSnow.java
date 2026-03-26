public class SmallSnow extends Surface {
    public SmallSnow(Lane lane) {
        super(lane);
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
}
