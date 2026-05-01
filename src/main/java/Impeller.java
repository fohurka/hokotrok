public class Impeller extends Equipment {
    public Impeller(SnowplowPlayer owner) {
        super(owner);
    }

    /**
     * Activates the Impeller on the specified lane.
     * This method specializes in clearing snow from the lane and
     * registers the equipment's owner as the one who performed the clearance.
     * * @param lane The lane to be cleared of snow.
     */
    @Override
    public void use(Lane lane) {
        int snowAmount = lane.clearSnow();
        lane.clearGrit();
        lane.cleared(owner, snowAmount);
    }

    @Override
    public void setAmmo(int ammo) {
        // Does nothing
    }
}
