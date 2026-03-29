public class Salter extends Equipment {
    public Salter(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("Salter.ctor");
        Skeleton.printReturn();
    }

    /**
     * Deploys salt on the specified lane.
     * This method applies a salting effect to the lane and registers
     * the equipment's owner as the individual responsible for the maintenance.
     * * @param lane The lane to be salted.
     */
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Salter.use");

        lane.salt();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
