import java.util.ArrayList;
import java.util.List;

public class Junction extends MapComponent {
    private List<Lane> starting;
    private List<Lane> ending;

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

    public void addEnding(Lane lane) {
        ending.add(lane);
    }

    public void addStarting(Lane lane) {
        starting.add(lane);
    }
}
