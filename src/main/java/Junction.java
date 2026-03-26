public class Junction extends MapComponent {

    public Junction() {
    }

    /**
     * Progresses a snowplow that is currently at this junction.
     * Does nothing.
     *
     * @param sp The snowplow that is progressing.
     */
    @Override
    public void progress(Snowplow sp) {
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
    }

    /**
     * Progresses a civil vehicle that is currently at this junction.
     * Does nothing.
     *
     * @param cv The civil vehicle that is progressing.
     */
    @Override
    public void progress(CivilVehicle cv) {
        Skeleton.printFunctionCall("Junction.progress");
        Skeleton.printReturn();
    }
}
