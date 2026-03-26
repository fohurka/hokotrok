/**
 * Represents a player who controls a single car in the game.
 * The car is controlled by an NPC.
 * The car travels between a home building and a work building.
 */
public class CarPlayer extends Player {

    private Car car;
    private Building home;
    private Building work;

    /**
     * Constructs a new CarPlayer with the given home and work buildings.
     * The car starts at the home building.
     *
     * @param home The starting home building.
     * @param work The destination work building.
     */
    public CarPlayer(Building home, Building work) {
        this.home = home;
        this.work = work;
        this.car = new Car(home);
    }

    /**
     * Gets the car controlled by this player.
     *
     * @return The car instance.
     */
    public Car getCar() {
        Skeleton.printFunctionCall("CarPlayer.getCar");
        Skeleton.printReturn();
        return car;
    }

    /**
     * Chooses the next direction(Lane or Building) for the car to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the vehicle, which must be 0 since the player
     *              controls one car.
     * @throws IllegalArgumentException if the index is not 0.
     */
    @Override
    public void choseDirection(MapComponent dest, int index) {
        Skeleton.printFunctionCall("CarPlayer.choseDirection");
        if (index != 0) {
            throw new IllegalArgumentException("Car player can only control one car");
        }
        car.setLocation(dest);
        Skeleton.printReturn();
    }
}
