public class Building extends MapComponent {
    public Building(Skeleton s) {
        super(s);
    }

    @Override
    public void progress(Snowplow sp) {
        s.printFunctionCall("Building.progress(Snowplow)");
        s.printReturn();
    }

    @Override
    public void progress(CivilVehicle cv) {
        s.printFunctionCall("Building.progress(CivilVehicle)");
        s.printReturn();
    }
}
