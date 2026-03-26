public class Junction extends MapComponent {

    public Junction(Skeleton s) {
        super(s);
    }

    @Override
    public void progress(Snowplow sp) {
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
    }

    @Override
    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
    }
}
