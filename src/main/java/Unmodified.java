public class Unmodified extends Modifier {

    @Override
    public void applyWeather(Surface surf) {
        Skeleton.printFunctionCall("Unmodified.applyWeather");
        surf.addSnow(Skeleton.askInt("Unmodified.applyWeather: amount of snow to add"));
        Skeleton.printReturn();
    }
}
