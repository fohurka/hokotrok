public class Salter extends Equipment {
    private int ammo;

    public Salter(SnowplowPlayer owner) {
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
     * Deploys salt on the specified lane.
     * This method applies a salting effect to the lane and registers
     * the equipment's owner as the individual responsible for the maintenance.
     * * @param lane The lane to be salted.
     */
    @Override
    public void use(Lane lane) {
        int prevSaltAmount = ammo;
        lane.salt();
        int usedSalt = prevSaltAmount - ammo;
        lane.cleared(owner, usedSalt);
    }
}
