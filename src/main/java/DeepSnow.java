public class DeepSnow extends Surface {
    public DeepSnow(Lane lane) {
        super(lane);
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        Skeleton.printFunctionCall("DeepSnow.calculateProgress");
        cv.stuckInCurrentLane(getLane());
        Skeleton.printReturn();
        return 0;
    }

    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("DeepSnow.enterable");
        Skeleton.printReturn();
        return false;
    }
}
