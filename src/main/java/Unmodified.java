public class Unmodified extends Modifier {

    /**
     * Applies the modifier's weather effect to the surface.
     * In the unmodified state, it adds a standard amount of snow.
     *
     * @param s the surface to apply the weather effect to
     */
    @Override
    protected void applyWeather(Surface s) {
        s.addSnow(1);
    }
}
