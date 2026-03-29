public class Impeller extends Equipment {
    public Impeller(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("Impeller.ctor");
        Skeleton.printReturn();
    }

    /**
     * Activates the Impeller on the specified lane.
     * This method specializes in clearing snow from the lane and
     * registers the equipment's owner as the one who performed the clearance.
     * * @param lane The lane to be cleared of snow.
     */
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Impeller.use");

        int snowAmount = lane.clearSnow();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
