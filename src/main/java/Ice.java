public class Ice extends Surface {
    private final Skeleton s;
    public Ice(Skeleton skeleton) {
        this.s = skeleton;
    }

    @Override
    public int calculateProgress(CivilVehicle cv) {
        boolean slip = s.askBool("Does the vehicle slip?");
    }
}
