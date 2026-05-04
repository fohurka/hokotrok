import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoadNetwork {
    private Bank bank;
    private List<Lane> lanes = new ArrayList<>();
    private List<Junction> junctions = new ArrayList<>();

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
        if (bank != null) {
            int amount = 5;
            bank.pay(p, amount);
        }
    }
    /**
     * Notifies the network that a snowplow has successfully cleared snow from a lane.
     * The network then instructs the bank to pay the reward based on the amount cleared.
     *
     * @param p The player controlling the snowplow.
     * @param snowAmount The amount of snow cleared.
     */
    public void laneCleared(Player p, int snowAmount) {
        if (bank != null) {
            bank.pay(p, snowAmount);
        }
    }

    /**
     * Randomly selects a specified number of unique junctions from the road network.
     *
     * @param n The number of unique junctions to select.
     * @return A list of the selected junctions.
     */
    private List<Junction> selectJunctions(int n) {
        List<Junction> selected = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Random rand = new Random();
            Junction j = null;
            do {
                j = junctions.get(rand.nextInt(junctions.size()));
            } while (selected.contains(j));
            selected.add(j);
        }
        return selected;
    }

    /**
     * Selects two unique junctions and attaches a new building to each of them.
     *
     * @return A list containing the two newly created buildings.
     */
    public List<Building> generateBuildingPair() {
        List<Building> buildings = new ArrayList<>();
        List<Junction> junctions = selectJunctions(2);
        for (Junction junction : junctions) {
            Building b = new Building(junction);
            buildings.add(b);
            junction.addBuilding(b);
        }
        return buildings;
    }

    /**
     * Advances the state of all lanes in the road network by one time step.
     */
    public void tick() {
        for (Lane lane : lanes) {
            lane.tick();
        }
    }

    /**
     * Adds a lane to the road network.
     *
     * @param lane The lane to be added.
     */
    public void addLane(Lane lane) {
        lanes.add(lane);
    }

    /**
     * Adds a junction to the road network.
     *
     * @param j The junction to be added.
     */
    public void addJunction(Junction j) {
        junctions.add(j);
    }
}
