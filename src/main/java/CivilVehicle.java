public abstract class CivilVehicle extends Vehicle {
    public abstract void stuckInCurrentLane(Lane lane);

    public abstract void slip(Lane lane);

    public abstract void crash();

    public abstract void tick();

    public abstract boolean pushable();
}