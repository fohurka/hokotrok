public class Salter extends Equipment {
    public Salter(SnowplowPlayer owner) {
        super(owner);
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Salter.use");

        int amount = 0;
        lane.salt();
        lane.cleared(owner, amount);
        
        Skeleton.printReturn();
    }
}
