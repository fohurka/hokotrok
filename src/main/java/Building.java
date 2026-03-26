public class Building extends MapComponent {
    public Building() {}

    @Override
    public void progress(Snowplow sp) {
        Skeleton.printFunctionCall("Building.progress");
        Skeleton.printReturn();
    }

    @Override
    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Building.progress");
        Skeleton.printReturn();
    }
}
