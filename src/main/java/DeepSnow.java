public class DeepSnow extends Surface {
    public DeepSnow(Skeleton skeleton, Lane lane) {
        super(skeleton, lane);
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        cv.stuckInCurrentLane(getLane());
        return 0;
    }

    @Override
    public boolean enterable() {
        return false;
    }
}
