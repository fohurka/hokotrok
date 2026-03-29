public class DragonBlade extends Equipment {
    public DragonBlade(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("DragonBlade.ctor");
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("DragonBlade.use");

        int iceAmount = lane.clearIce();
        int snowAmount = lane.clearSnow();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
