public class DragonBlade extends Equipment {
    public DragonBlade(SnowplowPlayer owner) {
        super(owner);
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
