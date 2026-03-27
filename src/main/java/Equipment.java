public abstract class Equipment {
    protected SnowplowPlayer owner;

    public Equipment(SnowplowPlayer owner) {
        Skeleton.printFunctionCall("Equipment.ctor");
        this.owner = owner;
        Skeleton.printReturn();
    }
    public abstract void use(Lane lane);
}
