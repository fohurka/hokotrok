public class IceBreaker extends Equipment {
    public IceBreaker(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("IceBreaker.ctor");
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("IceBreaker.use");

        int iceAmount = lane.clearIce();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
