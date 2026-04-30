public class Gritter extends Equipment {
    public Gritter(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Activates the Gritter on a specific lane.
     * This method applies grit to the lane and notifies the lane that it has been treated by the equipment's owner.
     * * @param lane The lane object where the treatment is performed.
     */
    @Override
    public void use(Lane lane) {
        lane.grit();
        lane.cleared(owner);
    }
}
