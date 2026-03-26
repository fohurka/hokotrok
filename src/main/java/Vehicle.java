public abstract class Vehicle {
    private MapComponent loc;

    public abstract void crash();

    public abstract void tick();

    public abstract boolean pushable();

    public Vehicle(MapComponent loc) {
        this.loc = loc;
    }

    public void setLocation(MapComponent dest) {
        Skeleton.printFunctionCall("Vehicle.setLocation");
        loc.remove(this);
        dest.arrived(this);
        Skeleton.printReturn();
    }

    protected MapComponent getLocation() {
        Skeleton.printFunctionCall("Vehicle.getLocation");
        Skeleton.printReturn();
        return loc;
    }
}