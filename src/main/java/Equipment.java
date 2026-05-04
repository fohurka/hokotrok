public abstract class Equipment {
    protected SnowplowPlayer owner;
    protected String id;

    /**
     * Constructs a new Equipment piece assigned to a snowplow player.
     *
     * @param owner the player who owns the snowplow using this equipment
     */
    public Equipment(SnowplowPlayer owner) {
        this.owner = owner;
    }

    /**
     * Returns the unique identifier of this equipment.
     *
     * @return the equipment ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier for this equipment.
     *
     * @param id the new equipment ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Activates the equipment on a specific lane.
     * Concrete implementations define how the equipment interacts with the lane surface.
     *
     * @param lane the lane object where the equipment is used
     */
    public abstract void use(Lane lane);

    /**
     * Sets the amount of ammo for the equipment, if applicable.
     *
     * @param ammo the amount of ammo to set
     */
    public abstract void setAmmo(int ammo);
}
