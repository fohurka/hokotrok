public class Building extends MapComponent {

    private Junction connection;

    public Building(Junction j) {
        super();
        this.connection = j;
    }

    /**
     * Progresses a snowplow that is currently at this building.
     * Does nothing.
     *
     * @param sp The snowplow that is progressing.
     */
    @Override
    public void progress(Snowplow sp) {
        Skeleton.printFunctionCall("Building.progress");
        Skeleton.printReturn();
    }

    /**
     * Progresses a civil vehicle that is currently at this building.
     * Does nothing.
     * 
     * @param cv The civil vehicle that is progressing.
     */
    @Override
    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Building.progress");
        Skeleton.printReturn();
    }
}
