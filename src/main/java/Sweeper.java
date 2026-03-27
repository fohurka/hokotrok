public class Sweeper extends Equipment {

    public Sweeper(SnowplowPlayer owner) {
        Skeleton.printFunctionCall("Sweeper.ctor");
        super(owner);
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Sweeper.use");

        int amount = 0;
        int snowAmount = lane.clearSnow();
        Lane lane2 = lane.getRightNeighbor();
        lane2.addSnow(snowAmount);
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
