public abstract class Surface {
    private Lane lane;
    protected final Skeleton s;

    protected Lane getLane() {
        return lane;
    }

    public Surface(Skeleton s, Lane lane) {
        this.lane = lane;
        this.s = s;
    }

    public int calculateProgress(Snowplow sn) {
        return 1;
    }

    public abstract int calculateProgress(CivilVehicle cv);
    public abstract boolean enterable();
}
