public class Ice extends Surface {
    public Ice(Skeleton skeleton, Lane lane) {
        super(skeleton, lane);
    }

    @Override
    public int calculateProgress(CivilVehicle cv) {
        boolean slip = s.askBool("Does the vehicle slip?");
        if (slip) {
            cv.slip();
            return 0;
        }
        return 1;
    }

    @Override
    public boolean enterable() {
        return true;
    }
}
