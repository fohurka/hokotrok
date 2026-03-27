public abstract class Equipment {
    protected SnowplowPlayer owner;

    public Equipment(SnowplowPlayer owner) {
        this.owner = owner;
    }
    public abstract void use(Lane lane);
}
