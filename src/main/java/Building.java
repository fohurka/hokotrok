public class Building extends MapComponent {

    private Junction connection;

    /**
     * Constructs a new Building connected to the specified Junction.
     *
     * @param j The Junction this building is connected to.
     */
    public Building(Junction j) {
        super();
        this.connection = j;
    }

    public Junction getConnection() {
        return connection;
    }

    /**
     * Progresses a civil vehicle that is currently at this building.
     * Does nothing.
     *
     * @param cv The civil vehicle that is progressing.
     */
    @Override
    public void progress(CivilVehicle cv) { }

    /**
     * Progresses a snowplow that is currently at this building.
     * Does nothing.
     *
     * @param sp The snowplow that is progressing.
     */
    @Override
    public void progress(Snowplow sp) { }
}
