public class IceBreaker extends Equipment {
    public IceBreaker(SnowplowPlayer owner) {
        Skeleton.printFunctionCall("IceBreaker.ctor");
        super(owner);
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("IceBreaker.use");

        int amount = 0;
        int iceAmount = lane.clearIce();
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
