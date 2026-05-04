/**
 * Represents a salter equipment that can be mounted on a snowplow.
 * It is used to apply salt to lanes, reducing ice and snow.
 */
public class Salter extends Equipment {
    private int ammo;

    /**
     * Constructs a Salter with a specified owner.
     * 
     * @param owner The SnowplowPlayer who owns this equipment.
     */
    public Salter(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Returns the current amount of salt ammo available.
     * 
     * @return The current ammo amount.
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Sets the amount of salt ammo available.
     * 
     * @param ammo The ammo amount to set.
     */
    @Override
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Deploys salt on the specified lane.
     * This method applies a salting effect to the lane, reduces ammo based on lane length,
     * and registers the equipment's owner as the individual responsible for the maintenance.
     * 
     * @param lane The lane to be salted.
     */
    @Override
    public void use(Lane lane) {
        lane.salt();
        ammo -= lane.getLength();
        lane.cleared(owner, lane.getLength());
    }
}
