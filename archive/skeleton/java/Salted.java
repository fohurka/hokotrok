public class Salted extends Modifier {


    /**
     * Applies the modifiers weather effect to the surface
     */
    @Override
    protected void applyWeather(Surface s) {
        Skeleton.printFunctionCall("Salted.applyWeather");
        s.removeIce(1);
        s.removeSnow(1);
        Skeleton.printReturn();
    }
}
