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

    /**
     * Retrieves the Junction to which this building is connected.
     *
     * @return The connected Junction.
     */
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
     * Progresses a car that is currently at this building.
     * Triggers the NPC logic for the car's owner.
     *
     * @param c The car that is progressing.
     */
    @Override
    public void progress(Car c) {
        c.getOwner().NPCLogic();
    }

    /**
     * Progresses a snowplow that is currently at this building.
     * Does nothing.
     *
     * @param sp The snowplow that is progressing.
     */
    @Override
    public void progress(Snowplow sp) { }
}
