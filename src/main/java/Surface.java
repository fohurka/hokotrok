public abstract class Surface {
    private Lane lane;
    private Modifier modifier;

    protected Lane getLane() {
        return lane;
    }

    public Surface(Lane lane) {
        this.lane = lane;
    }

    public int calculateProgress(Snowplow sn) {
        Skeleton.printFunctionCall("Surface.calculateProgress");
        Skeleton.printReturn();
        return 1;
    }

    public void removeSnow(int amount) {
        Skeleton.printFunctionCall("Surface.removeSnow");
        Skeleton.printReturn();
    }

    public void removeIce(int amount) {
        Skeleton.printFunctionCall("Surface.removeIce");
        Skeleton.printReturn();
    }

    public int clearSnow() {
        Skeleton.printFunctionCall("Surface.clearSnow");
        Skeleton.printReturn();
        return Skeleton.askInt("Surface.clearSnow return value: ");
    }
    
    public int clearIce() {
        Skeleton.printFunctionCall("Surface.clearIce");
        Skeleton.printReturn();
        return Skeleton.askInt("Surface.clearIce return value: ");
    }

    public void addSnow(int amount) {
        Skeleton.printFunctionCall("Surface.addSnow");
        Skeleton.printReturn();
    }

    public void setSalted() {
        Skeleton.printFunctionCall("Surface.setSalted");
        Skeleton.printReturn();
    }

    public abstract int calculateProgress(CivilVehicle cv);
    public abstract boolean enterable();
}
