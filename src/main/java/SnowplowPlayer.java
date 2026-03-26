import java.util.List;

public class SnowplowPlayer extends Player {

    private List<Snowplow> snowplows;

    public SnowplowPlayer(List<Snowplow> snowplows) {
        this.snowplows = snowplows;
    }

    @Override
    public void choseDirection(MapComponent dest, int index) {
        Skeleton.printFunctionCall("SnowplowPlayer.choseDirection");
        if (index < 0 || index >= snowplows.size()) {
            throw new IllegalArgumentException("Invalid snowplow index");
        }
        snowplows.get(index).setLocation(dest);
        Skeleton.printReturn();
    }
}