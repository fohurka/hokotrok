public class DragonBlade extends Equipment {
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
