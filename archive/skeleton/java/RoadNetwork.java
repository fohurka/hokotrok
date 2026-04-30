public class RoadNetwork {
    private Bank bank;

    /**
     * Sets the bank used by the road network to pay rewards to players.
     *
     * @param bank The bank to be set.
     */
    public void setBank(Bank bank) {
        this.bank = bank;
    }
    /**
     * Notifies the network that a bus has completed its round.
     * The network then instructs the bank to pay the reward to the player.
     *
     * @param p The player controlling the bus.
     */
    public void busRoundComplete(Player p) {
        Skeleton.printFunctionCall("RoadNetwork.busRoundComplete");
        if (bank != null) {
            int amount = 100; // Dummy reward amount for skeleton
            bank.pay(p, amount);
        }
        Skeleton.printReturn();
    }
    /**
     * Notifies the network that a snowplow has successfully cleared snow from a lane.
     * The network then instructs the bank to pay the reward based on the amount cleared.
     *
     * @param p The player controlling the snowplow.
     * @param snowAmount The amount of snow cleared.
     */
    public void laneCleared(Player p, int snowAmount) {
        Skeleton.printFunctionCall("RoadNetwork.laneCleared");
        if (bank != null) {
            bank.pay(p, snowAmount);
        }
        Skeleton.printReturn();
    }
}
