/**
 * A modifier that represents a salted surface.
 * It reduces the amount of ice and snow on the surface when weather effects are applied.
 */
public class Salted extends Modifier {


    /**
     * Applies the salted weather effect to the surface.
     * Decreases ice and snow amounts by 1 unit if they are greater than 0.
     * 
     * @param s The surface to apply the effect to.
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
