public class DragonBlade extends Equipment {
    public DragonBlade(SnowplowPlayer owner) {
        Skeleton.printFunctionCall("DragonBlade.ctor");
        super(owner);
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("DragonBlade.use");

        int amount = 0;
        int iceAmount = lane.clearIce();
        int snowAmount = lane.clearSnow();
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
