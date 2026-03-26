public abstract class Vehicle {
    private MapComponent loc;

    public abstract void crash();

    public abstract void tick();

    public abstract boolean pushable();

    public void setLocation(MapComponent dest) {
        Skeleton.printFunctionCall("Vehicle.setLocation");
        if (loc != null) loc.remove(this);
        dest.arrived(this);
        loc = dest;
        Skeleton.printReturn();
    }

    protected MapComponent getLocation() {
        Skeleton.printFunctionCall("Vehicle.getLocation");
        Skeleton.printReturn();
        return loc;
    }
}