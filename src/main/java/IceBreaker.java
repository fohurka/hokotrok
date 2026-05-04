/**
 * Represents an ice breaker equipment that can be mounted on a snowplow.
 * It is used to break ice into snow on lanes.
 */
public class IceBreaker extends Equipment {
    /**
     * Constructs a new IceBreaker equipment assigned to a snowplow player.
     *
     * @param owner The player who owns the snowplow using this equipment.
     */
    public IceBreaker(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Uses the ice breaker equipment to clear ice from the specified lane.
     * Removes the ice amount from the lane, adds it as snow, and updates its state as cleared by the
     * owner.
     *
     * @param lane The lane from which ice should be removed.
     */
    @Override
    public void use(Lane lane) {
        int iceAmount = lane.clearIce();
        lane.addSnow(iceAmount);
        lane.cleared(owner, iceAmount);
    }

    /**
     * Sets the ammo for the ice breaker. This equipment does not use ammo,
     * so this method has no effect.
     * 
     * @param ammo The ammo amount to set (ignored).
     */
    @Override
    public void setAmmo(int ammo) {
        // Does nothing
    }
}
