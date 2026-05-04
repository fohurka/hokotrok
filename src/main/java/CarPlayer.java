import java.util.ArrayList;
import java.util.List;

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
        this.car = new Car(this);
    }

    /**
     * Gets the car controlled by this player.
     *
     * @return The car instance.
     */
    public Car getCar() {
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
        if (index != 0) {
            throw new IllegalArgumentException("Car player can only control one car");
        }

        Junction _dest = ((Building) dest).getConnection();
        MapComponent loc = car.getLocation();
        List<Junction> visited = new ArrayList<>();
        visited.add(_dest);
        List<Junction> q = new ArrayList<>();
        q.add(_dest);
        while (!q.isEmpty()) {
            Junction curr = q.remove(0);
            for (Lane l : curr.getLanes()) {
                Junction next = (l.getStart() == curr) ? l.getEnd() : l.getStart();
                if (!visited.contains(next) && l.enterable()) {
                    if (next == loc) {
                        car.setLocation(l);
                        return;
                    }
                    visited.add(next);
                    q.add(next);
                }
            }
        }
    }

    /**
     * Executes the NPC logic for the car.
     * Determines the next movement of the car based on its current location relative to home and work.
     */
    public void NPCLogic() {
        MapComponent loc = car.getLocation();
        if(loc == home) {
            car.setLocation(home.getConnection());
        } else if (loc == work) {
            car.setLocation(work.getConnection());
            Building tmp = home;
            home = work;
            work = tmp;
        } else if (loc == work.getConnection()) {
            car.setLocation(work);
        } else {
            choseDirection(work, 0);
        }
    }

    /**
     * Brings the car home
     */
    public void goHome() {
        car.setLocation(home);
    }

    /**
     * Retrieves the list of vehicles controlled by this player.
     * For a CarPlayer, this list contains only the car.
     *
     * @return A list containing the car controlled by the player.
     */
    public List<Vehicle> getVehicles() {
        List<Vehicle> r = new ArrayList<Vehicle>();
        r.add(car);
        return r;
    }

    /**
     * Retrieves the home building of the player.
     *
     * @return The home Building.
     */
    public Building getHome() {
        return home;
    }

    /**
     * Retrieves the work building of the player.
     *
     * @return The work Building.
     */
    public Building getWork() {
        return work;
    }
}