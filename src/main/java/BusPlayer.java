/**
 * Represents a player who controls a single bus in the game.
 */
public class BusPlayer extends Player {

    private Bus bus;

    /**
     * Constructs a new BusPlayer with a bus starting at the given junction.
     *
     * @param starter The junction where the bus initially spawns.
     */
    public BusPlayer(Junction starter) {
        this.bus = new Bus(starter);
    }

    /**
     * Gets the bus controlled by this player.
     *
     * @return The bus instance.
     */
    public Bus getBus() {
        Skeleton.printFunctionCall("BusPlayer.getBus");
        Skeleton.printReturn();
        return bus;
    }

    /**
     * Chooses the next direction(Lane or Building) for the bus to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the vehicle, which must be 0 since the player
     *              controls one bus.
     * @throws IllegalArgumentException if the index is not 0.
     */
    @Override
    public void choseDirection(MapComponent dest, int index) {
        Skeleton.printFunctionCall("BusPlayer.choseDirection");
        if (index != 0) {
            throw new IllegalArgumentException("Bus player can only control one bus");
        }
        bus.setLocation(dest);
        Skeleton.printReturn();
    }
}
