public class DragonBlade extends Equipment {
    private int ammo;

    /**
     * Constructs a new DragonBlade equipment assigned to a snowplow player.
     *
     * @param owner the player who owns the snowplow using this equipment
     */
    public DragonBlade(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Returns the current amount of ammo remaining in the DragonBlade.
     *
     * @return the current ammo count
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Sets the amount of ammo for the DragonBlade.
     *
     * @param ammo the new ammo count
     */
    @Override
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Activates the DragonBlade on a specific lane.
     * This method clears all existing ice and snow from the lane and
     * notifies the lane that it has been cleared by the equipment's owner.
     *
     * @param lane the lane object where the clearing action is performed
     */
    @Override
    public void use(Lane lane) {
        int iceAmount = lane.clearIce();
        int snowAmount = lane.clearSnow();
        lane.cleared(owner, iceAmount + snowAmount);
        ammo -= 1;
    }
}
