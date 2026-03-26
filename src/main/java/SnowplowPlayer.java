import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player who controls snowplows in the game.
 */
public class SnowplowPlayer extends Player {

    private List<Snowplow> snowplows;

    /**
     * Constructs a new SnowplowPlayer and initializes a snowplow at the given
     * junction.
     *
     * @param starter The junction where the initial snowplow spawns.
     */
    public SnowplowPlayer(Junction starter) {
        this.snowplows = new ArrayList<>();
        snowplows.add(new Snowplow(starter));
    }

    /**
     * Gets a specific snowplow controlled by this player.
     *
     * @param index The index of the snowplow to retrieve.
     * @return The snowplow at the specified index.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    public Snowplow getSnowplow(int index) {
        Skeleton.printFunctionCall("SnowplowPlayer.getSnowplow");
        if (index < 0 || index >= snowplows.size()) {
            throw new IllegalArgumentException("Invalid snowplow index");
        }
        Skeleton.printReturn();
        return snowplows.get(index);
    }

    /**
     * Chooses the next direction(Lane or Building) for a specific snowplow to take.
     *
     * @param dest  The destination map component.
     * @param index The index of the snowplow to command.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
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