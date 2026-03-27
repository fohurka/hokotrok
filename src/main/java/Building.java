public class Building extends MapComponent {

    private Junction connection;

    public Building(Junction j) {
        super();
        this.connection = j;
    }
    /**
     * Checks if this building is the target destination for the given bus.
     * In the skeleton, it asks the tester directly.
     *
     * @param bus The bus arriving at the building's junction.
     * @return true if it is the target destination, false otherwise.
     */
    public boolean isTargetBusStop(Bus bus) {
        Skeleton.printFunctionCall("Building.isTargetBusStop");

        boolean isTarget = Skeleton.askBool("A busz a celallomasara erkezett?");

        Skeleton.printReturn();
        return isTarget;
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
