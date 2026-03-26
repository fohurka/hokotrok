public class Junction extends MapComponent {
    public Junction(Skeleton s) {
        super(s);
    }

    @Override
    public void progress(Snowplow sp) {
        s.printFunctionCall("Junction.progress(Snowplow)");
        s.printReturn();
    }

    @Override
    public void progress(CivilVehicle cv) {
        s.printFunctionCall("Junction.progress(CivilVehicle)");
        s.printReturn();
    }
}
