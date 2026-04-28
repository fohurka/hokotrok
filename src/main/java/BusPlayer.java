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
        this.bus = new Bus();
        this.bus.setOwner(this);
        this.bus.setLocation(starter);
    }

    /**
     * Gets the bus controlled by this player.
     *
     * @return The bus instance.
     */
    public Bus getBus() {
        return bus;
    }

    /**
     * Sets the single bus controlled by this player.
     * Used mainly during the skeleton test setup phase to inject a specific bus.
     *
     * @param bus The bus to be assigned to the player.
     */
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    /**
     * Chooses the next direction(Lane or Building) for the bus to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the vehicle, which must be 0 since the player
     * controls one bus.
     * @throws IllegalArgumentException if the index is not 0.
     */
    @Override
    public void choseDirection(MapComponent dest, int index) {
        if (index != 0) {
            throw new IllegalArgumentException("Bus player can only control one bus");
        }
        bus.setLocation(dest);
    }
}