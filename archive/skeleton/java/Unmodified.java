public class Unmodified extends Modifier {

    /**
     * Applies the modifiers weather effect to the surface
     */
    @Override
    protected void applyWeather(Surface s) {
        Skeleton.printFunctionCall("Unmodified.applyWeather");
        s.addSnow(1);
        Skeleton.printReturn();
    }
}
