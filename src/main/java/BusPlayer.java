public class BusPlayer extends Player {

    private Bus bus;

    public BusPlayer(Bus bus) {
        this.bus = bus;
    }

    @Override
    public void choseDirection(MapComponent dest, int index) {
        Skeleton.printFunctionCall("BusPlayer.choseDirection");
        if (index != 0) {
            throw new IllegalArgumentException("Bus player can only control one bus");
        }
        bus.setLocation(dest);
        Skeleton.printReturn();
    }
}
