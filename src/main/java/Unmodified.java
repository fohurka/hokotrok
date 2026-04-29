public class Unmodified extends Modifier {

    /**
     * Applies the modifiers weather effect to the surface
     */
    @Override
    protected void applyWeather(Surface s) {
        s.addSnow(1);
    }
}
