public class Gritter extends Equipment {
    private int ammo;

    /**
     * Constructs a new Gritter equipment assigned to a snowplow player.
     *
     * @param owner the player who owns the snowplow using this equipment
     */
    public Gritter(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Returns the current amount of ammo remaining in the Gritter.
     *
     * @return the current ammo count
     */
    public int getAmmo() {
        return ammo;
    }

    /**
     * Sets the amount of ammo for the Gritter.
     *
     * @param ammo the new ammo count
     */
    @Override
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Activates the Gritter on a specific lane.
     * This method applies grit to the lane and notifies the lane that it has been
     * treated by the equipment's owner.
     *
     * @param lane the lane object where the treatment is performed
     */
    @Override
    public void use(Lane lane) {
        int prevGritAmount = ammo;
        lane.grit();
        int usedGrit = prevGritAmount - ammo;
        lane.cleared(owner, usedGrit);
        ammo -= 1;
    }
}
