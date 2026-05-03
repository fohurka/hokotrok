public class Gritter extends Equipment {
    private int ammo;

    public Gritter(SnowplowPlayer owner) {
        super(owner);
    }

    public int getAmmo() {
        return ammo;
    }

    @Override
    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    /**
     * Activates the Gritter on a specific lane.
     * This method applies grit to the lane and notifies the lane that it has been
     * treated by the equipment's owner.
     * * @param lane The lane object where the treatment is performed.
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
