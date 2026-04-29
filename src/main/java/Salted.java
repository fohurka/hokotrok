public class Salted extends Modifier {


    /**
     * Applies the modifiers weather effect to the surface
     */
    @Override
    protected void applyWeather(Surface s) {
        s.removeIce(1);
        s.removeSnow(1);
    }
}
