public class DeepSnow extends Surface {
    private Skeleton s;
    public DeepSnow(Skeleton skeleton) {
        this.s = skeleton;
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        cv.stuckInCurrentLane();
        return 0;
    }
}
