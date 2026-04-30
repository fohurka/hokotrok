public class Sweeper extends Equipment {
    public Sweeper(SnowplowPlayer owner) {
        super(owner);
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
        int snowAmount = lane.clearSnow();
        lane.clearGrit();
        Lane rightNeighbor = lane.getRightNeighbor();
        rightNeighbor.addSnow(snowAmount);
        rightNeighbor.addGrit();
        lane.cleared(owner);
    }
}
