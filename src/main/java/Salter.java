public class Salter extends Equipment {
    public Salter(SnowplowPlayer owner) {
        Skeleton.printFunctionCall("Salter.ctor");
        super(owner);
        Skeleton.printReturn();
    }

    @Override
    public void use(Lane lane) {
        Skeleton.printFunctionCall("Salter.use");

        lane.salt();
        lane.cleared(owner);
        
        Skeleton.printReturn();
    }
}
