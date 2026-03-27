public class Salted extends Modifier {

    @Override
    public void applyWeather(Surface surf) {
        Skeleton.printFunctionCall("Salted.applyWeather");
        surf.removeIce(Skeleton.askInt("Salted.applyWeather: amount of ice to remove"));
        Skeleton.printReturn();
    }
}
