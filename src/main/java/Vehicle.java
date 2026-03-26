public abstract class Vehicle {
    private MapComponent loc;
    protected final Skeleton s;

    public abstract void crash();

    public abstract void tick();

    public abstract boolean pushable();

    public Vehicle(Skeleton s) {
        this.s = s;
    }

    public void setLocation(MapComponent dest) {
        loc.remove(this);
        dest.arrived(this);
    }

    protected MapComponent getLocation() {
        return loc;
    }
}