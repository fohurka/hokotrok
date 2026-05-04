import java.util.ArrayList;
import java.util.List;

public class Junction extends MapComponent {
    private List<Lane> starting;
    private List<Lane> ending;
    private RoadNetwork rn;
    private List<Building> buildings = new ArrayList<>();

    /**
     * Constructs a new Junction with empty lists of starting and ending lanes.
     */
    public Junction() {
        starting = new ArrayList<Lane>();
        ending = new ArrayList<Lane>();
    }

    /**
     * Progresses a snowplow that is currently at this junction.
     * Does nothing.
     *
     * @param sp The snowplow that is progressing.
     */
    @Override
    public void progress(Snowplow sp) {
    }

    /**
     * Progresses a civil vehicle that is currently at this junction.
     * Does nothing.
     *
     * @param cv The civil vehicle that is progressing.
     */
    @Override
    public void progress(CivilVehicle cv) {
    }

    /**
     * Progresses a car that is currently at this junction.
     * Triggers the car owner's NPC logic.
     *
     * @param c The car that is progressing.
     */
    @Override
    public void progress(Car c) {
        c.getOwner().NPCLogic();
    }

    /**
     * Adds a Lane to the list of Lanes that are ending in this Junction
     *
     * @param lane the Lane to be added
     */
    public void addEnding(Lane lane) {
        ending.add(lane);
    }

    /**
     * Adds a Lane to the list of Lanes that are starting in this Junction
     *
     * @param lane the Lane to be added
     */
    public void addStarting(Lane lane) {
        starting.add(lane);
    }

    /**
     * Sets the road network that this junction is a part of.
     *
     * @param rn The road network to be set.
     */
    public void setRoadNetwork(RoadNetwork rn) {
        this.rn = rn;
    }

    /**
     * Adds a building to the junction.
     *
     * @param building The building to be added.
     */
    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    /**
     * Returns a combined list of all starting and ending lanes at this junction.
     *
     * @return A list containing all lanes connected to this junction.
     */
    public List<Lane> getLanes() {
        List<Lane> r = new ArrayList<>();
        r.addAll(starting); r.addAll(ending);
        return r;
    }

    /**
     * Returns the list of lanes that start at this junction.
     *
     * @return The list of starting lanes.
     */
    public List<Lane> getStartingLanes() { return starting; }

    /**
     * Returns the list of lanes that end at this junction.
     *
     * @return The list of ending lanes.
     */
    public List<Lane> getEndingLanes()   { return ending; }

    /**
     * Returns the list of buildings attached to this junction.
     *
     * @return The list of attached buildings.
     */
    public List<Building> getBuildings() { return buildings; }

    /**
     * Handles a bus arriving at the junction. It queries the attached building
     * to check if it's the target destination. If successful, it notifies
     * the road network to pay out the reward to the bus's owner.
     *
     * @param bus The bus arriving at the junction.
     */
    public void arrived(Bus bus) {
        boolean isTarget = false;

        if (bus.getStations() != null && bus.getStations().size() >= 2) {
            for (Building building : buildings) {
                if (building == bus.getStations().get(1)) {
                    isTarget = true;
                    break;
                }
            }
        }

        if (isTarget && rn != null) {
            BusPlayer bp = null;
            if (bus != null) {
                bp = bus.getOwner();
            }
            rn.busRoundComplete(bp);
        }
    }
}
