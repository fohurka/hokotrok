/**
 * Abstract base class for modifiers that can affect the surface of a lane or junction.
 */
public abstract class Modifier {

    /**
     * Applies a weather-related modification to the given surface.
     * 
     * @param s The surface to be modified.
     */
    protected abstract void applyWeather(Surface s);
}
