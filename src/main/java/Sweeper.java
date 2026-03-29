public class Sweeper extends Equipment {

    public Sweeper(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("Sweeper.ctor");
        Skeleton.printReturn();
    }

    /**
     * Activates the Sweeper on a specific lane.
     * Unlike other tools, the Sweeper clears snow from the current lane and
     * pushes that same amount onto the lane's right-hand neighbor.
     * Finally, it marks the original lane as cleared by the owner.
     * * @param lane The primary lane being swept.
     */
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Sweeper.use");

        int snowAmount = lane.clearSnow();
        Lane lane2 = lane.getRightNeighbor();
        lane2.addSnow(snowAmount);
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
