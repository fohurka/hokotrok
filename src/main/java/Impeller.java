public class Impeller extends Equipment {
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Impeller.use");

        int amount = 0;
        int snowAmount = lane.clearSnow();
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
