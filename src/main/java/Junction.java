import java.util.ArrayList;
import java.util.List;

public class Junction extends MapComponent {
    private List<Lane> starting;
    private List<Lane> ending;
    private RoadNetwork rn;
    private List<Building> buildings = new ArrayList<>();

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
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
    }

    /**
     * Progresses a civil vehicle that is currently at this junction.
     * Does nothing.
     *
     * @param cv The civil vehicle that is progressing.
     */
    @Override
    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
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
    public void addBuilding(Building building) {
        this.buildings.add(building);
    }
    /**
     * Handles a bus arriving at the junction. In the skeleton, the method
     * asks the tester if the bus has reached its target destination.
     * If successful, it notifies the road network to pay out the reward.
     *
     * @param bus The bus arriving at the junction.
     */
    /**
     * Handles a bus arriving at the junction. It queries the attached building
     * to check if it's the target destination. If successful, it notifies
     * the road network to pay out the reward to the bus's owner.
     *
     * @param bus The bus arriving at the junction.
     */
    public void arrived(Bus bus) {
        Skeleton.printFunctionCall("Junction.arrived");

        boolean isTarget = false;

        if (!buildings.isEmpty()) {
            for (int i=0;i<buildings.size();i++)
            {
                if(buildings.getFirst().isTargetBusStop(bus))
                {
                    isTarget = true;
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

        Skeleton.printReturn();
    }
}
