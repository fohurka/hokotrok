public class SmallSnow extends Surface {
    public SmallSnow(Lane lane) {
        super(lane);
    }
    @Override
    public int calculateProgress(CivilVehicle cv) {
        Skeleton.printFunctionCall("SmallSnow.calculateProgress");
        Skeleton.printReturn();
        return 1;
    }

    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("SmallSnow.enterable");
        Skeleton.printReturn();
        return true;
    }
}
