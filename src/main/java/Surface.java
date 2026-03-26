public abstract class Surface {
    private Lane lane;

    protected Lane getLane() {
        return lane;
    }

    public Surface(Lane lane) {
        this.lane = lane;
    }

    public int calculateProgress(Snowplow sn) {
        return 1;
    }

    public abstract int calculateProgress(CivilVehicle cv);
    public abstract boolean enterable();
}
