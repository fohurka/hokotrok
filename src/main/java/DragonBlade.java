public class DragonBlade extends Equipment {
    private int ammo;

    public DragonBlade(SnowplowPlayer owner) {
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
     * Activates the DragonBlade on a specific lane.
     * This method clears all existing ice and snow from the lane and
     * notifies the lane that it has been cleared by the equipment's owner.
     * * @param lane The lane object where the clearing action is performed.
     */
    @Override
    public void use(Lane lane) {
        int iceAmount = lane.clearIce();
        int snowAmount = lane.clearSnow();
        lane.cleared(owner);
    }
}
