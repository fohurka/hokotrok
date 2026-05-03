public class Salted extends Modifier {


    /**
     * Applies the modifiers weather effect to the surface
     */
    @Override
    protected void applyWeather(Surface s) {
        if(s.getIceAmount() > 0) {
            s.removeIce(1);
        }
        if(s.getSnowAmount() > 0) {
            s.removeSnow(1);
        }
    }
}
