public class DeepSnow extends Surface {
    public DeepSnow(Lane lane) {
        super(lane);
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
}
