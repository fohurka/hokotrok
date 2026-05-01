public abstract class Equipment {
    protected SnowplowPlayer owner;
    protected String id;

    public Equipment(SnowplowPlayer owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public abstract void use(Lane lane);

    public abstract void setAmmo(int ammo);
}
