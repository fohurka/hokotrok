public class Sweeper extends Equipment {

    // Skeleton.printFunctionCall("Lane.getLeftNeighbor");
    // Skeleton.printReturn();
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Sweeper.use");
        int snowAmount = lane.clearSnow();
        Lane lane2 = lane.getRightNeighbor();
        lane2.addSnow(snowAmount);
        lane.cleared(owner, snowAmount);
        Skeleton.printReturn();
    }
}
