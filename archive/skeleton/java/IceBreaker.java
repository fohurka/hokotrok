public class IceBreaker extends Equipment {
    /**
     * Constructs a new IceBreaker equipment assigned to a snowplow player.
     *
     * @param owner The player who owns the snowplow using this equipment.
     */
    public IceBreaker(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("IceBreaker.ctor");
        Skeleton.printReturn();
    }

    /**
     * Uses the ice breaker equipment to clear ice from the specified lane.
     * Removes the ice amount from the lane and updates its state as cleared by the owner.
     *
     * @param lane The lane from which ice should be removed.
     */
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("IceBreaker.use");

        int iceAmount = lane.clearIce();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
