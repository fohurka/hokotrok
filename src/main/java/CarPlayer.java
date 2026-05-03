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
    private Building nextDest;

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
        this.nextDest = work;
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
     * controls one car.
     * @throws IllegalArgumentException if the index is not 0.
     */
    @Override
    public void choseDirection(MapComponent dest, int index) {
        if (index != 0) {
            throw new IllegalArgumentException("Car player can only control one car");
        }
        car.setLocation(dest);


        Junction _dest = getNextDest().getConnection();
                MapComponent loc = car.getLocation();
        if(_dest == loc) {
            arrived();
            _dest = getNextDest().getConnection();
        }
        List<Junction> visited = new ArrayList<>();
        visited.add(_dest);
        List<Junction> q = new ArrayList<>();
        q.add(_dest);
        while (!q.isEmpty()) {
            Junction curr = q.remove(0);
            for(Lane l : curr.getLanes()) {
                Junction next = (l.getStart() == curr) ? l.getEnd() : l.getStart();
                if (!visited.contains(next) && l.enterable()) {
                    if(next == loc) {
                      car.setLocation(l);
                        return;
                    }
                    visited.add(next);
                    q.add(next);
                }
            }

        }
    }

    public void NPCLogic() {

        choseDirection(null, 0);


       
    }

    /**
     * Brings the car home
     */
    public void goHome() {
        car.setLocation(home);
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> r = new ArrayList<Vehicle>();
        r.add(car);
        return r;
    }

    public void arrived(){
        if (nextDest == work) {
            nextDest = home;
        } else {
            nextDest = work;
        }
    }

    public Building getHome() { return home; }
    public Building getWork() { return work; }
    public Building getNextDest() { return nextDest; }

}