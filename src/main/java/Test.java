public class Test {
    public abstract class TestCase {
        public abstract String getName();

        public abstract void setup();

        public abstract void execute();
    }

    public class JunctionProgressCar extends TestCase {
        private Junction loc;
        private Car c;

        @Override
        public String getName() {
            return "JunctionProgressCar";
        }

        @Override
        public void setup() {
            loc = new Junction();
            c = new Car(loc);
        }

        @Override
        public void execute() {
            c.tick();
        }
    }

    public class JunctionProgressBus extends TestCase {
        private Junction loc;
        private Bus b;

        @Override
        public String getName() {
            return "JunctionProgressBus";
        }

        @Override
        public void setup() {
            loc = new Junction();
            b = new Bus(loc);
        }

        @Override
        public void execute() {
            b.tick();
        }
    }

    public class JunctionProgressSnowplow extends TestCase {
        private Junction loc;
        private Snowplow sn;

        @Override
        public String getName() {
            return "JunctionProgressSnowplow";
        }

        @Override
        public void setup() {
            loc = new Junction();
            sn = new Snowplow(loc);
        }

        @Override
        public void execute() {
            sn.tick();
        }
    }

    public class JunctionChoiceCar extends TestCase {
        private CarPlayer p;
        private Lane dest;

        @Override
        public String getName() {
            return "JunctionChoiceCar";
        }

        @Override
        public void setup() {
            // TODO
        }

        @Override
        public void execute() {
            p.choseDirection(dest, 0);
        }
    }
}
