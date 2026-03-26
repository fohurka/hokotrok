public class Ice extends Surface {
    public Ice(Lane lane) {
        super(lane);
    }

    @Override
    public int calculateProgress(CivilVehicle cv) {
        boolean slip = Skeleton.askBool("Does the vehicle slip?");
        if (slip) {
            cv.slip(getLane());
            return 0;
        }
        return 1;
    }

    @Override
    public boolean enterable() {
        return true;
    }
}
