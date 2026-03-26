public abstract class Vehicle {
    private MapComponent loc;
    protected final Skeleton s;

    public abstract void crash();
    public abstract void tick();
    public abstract boolean pushable();

    public Vehicle(Skeleton s) {
        this.s = s;
    }

    public void setLocation(MapComponent newLocation) {
        loc.remove(this);
        newLocation.arrived(this);
    }

    protected MapComponent getLocation() {
        return loc;
    }
}