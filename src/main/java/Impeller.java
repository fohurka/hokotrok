/**
 * Represents an impeller equipment that can be mounted on a snowplow.
 * It is used to clear snow and grit from lanes.
 */
public class Impeller extends Equipment {
    /**
     * Constructs an Impeller with a specified owner.
     * 
     * @param owner The SnowplowPlayer who owns this equipment.
     */
    public Impeller(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Activates the Impeller on the specified lane.
     * This method specializes in clearing snow and grit from the lane and
     * registers the equipment's owner as the one who performed the clearance.
     * 
     * @param lane The lane to be cleared of snow and grit.
     */
    @Override
    public void use(Lane lane) {
        int snowAmount = lane.clearSnow();
        lane.clearGrit();
        lane.cleared(owner, snowAmount);
    }

    /**
     * Sets the ammo for the impeller. This equipment does not use ammo,
     * so this method has no effect.
     * 
     * @param ammo The ammo amount to set (ignored).
     */
    @Override
    public void setAmmo(int ammo) {
        // Does nothing
    }
}
