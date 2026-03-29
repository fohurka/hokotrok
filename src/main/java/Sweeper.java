public class Sweeper extends Equipment {

    public Sweeper(SnowplowPlayer owner) {
        super(owner);
        Skeleton.printFunctionCall("Sweeper.ctor");
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Sweeper.use");

        int snowAmount = lane.clearSnow();
        Lane lane2 = lane.getRightNeighbor();
        lane2.addSnow(snowAmount);
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
