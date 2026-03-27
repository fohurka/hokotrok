public class IceBreaker extends Equipment {
    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("IceBreaker.use");

        int amount = 0;
        int iceAmount = lane.clearIce();
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
