public class CarPlayer extends Player {

    private Car car;

    public CarPlayer(Car car) {
        this.car = car;
    }

    @Override
    public void choseDirection(MapComponent dest, int index) {
        Skeleton.printFunctionCall("CarPlayer.choseDirection");
        if (index != 0) {
            throw new IllegalArgumentException("Car player can only control one car");
        }
        car.setLocation(dest);
        Skeleton.printReturn();
    }
}
