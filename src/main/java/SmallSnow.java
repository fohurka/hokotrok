public class SmallSnow extends Surface {
    public SmallSnow(Skeleton skeleton, Lane lane) {
        super(skeleton, lane);
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        return 1;
    }

    @Override
    public boolean enterable() {
        return true;
    }
}
