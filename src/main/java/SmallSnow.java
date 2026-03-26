public class SmallSnow extends Surface {
    public SmallSnow(Lane lane) {
        super(lane);
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
