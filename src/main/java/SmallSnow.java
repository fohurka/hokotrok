public class SmallSnow extends Surface {
    private final Skeleton s;
    public SmallSnow(Skeleton skeleton) {
        this.s = skeleton;
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        return 1;
    }
}
