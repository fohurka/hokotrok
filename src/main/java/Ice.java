public class Ice extends Surface {
    public Ice(Lane lane) {
        super(lane);
    }

    @Override
    public int calculateProgress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Ice.calculateProgress");
        boolean slip = Skeleton.askBool("Does the vehicle slip?");
        if (slip) {
            cv.slip(getLane());
            Skeleton.printReturn();
            return 0;
        }
        Skeleton.printReturn();
        return 1;
    }

    @Override
    public boolean enterable() {
        Skeleton.printFunctionCall("Ice.enterable");
        Skeleton.printReturn();
        return true;
    }
}
