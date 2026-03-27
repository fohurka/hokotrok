public class Salted extends Modifier {

    @Override
    public void applyWeather(Surface surf) {
        Skeleton.printFunctionCall("Salted.applyWeather");
        surf.removeSnow(Skeleton.askInt("Salted.applyWeather: amount of snow to remove"));
        surf.removeIce(Skeleton.askInt("Salted.applyWeather: amount of ice to remove"));
        Skeleton.printReturn();
    }
}
