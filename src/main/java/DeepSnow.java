public class DeepSnow extends Surface {
    public DeepSnow(Lane lane) {
        super(lane);
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
