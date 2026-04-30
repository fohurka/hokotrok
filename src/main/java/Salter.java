public class Salter extends Equipment {
    public Salter(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Deploys salt on the specified lane.
     * This method applies a salting effect to the lane and registers
     * the equipment's owner as the individual responsible for the maintenance.
     * * @param lane The lane to be salted.
     */
    @Override
    public void use(Lane lane) {
        lane.salt();
        lane.cleared(owner);
    }
}
