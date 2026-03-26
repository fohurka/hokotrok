public class Test {
    public abstract class TestCase {
        public abstract void execute();
    }

    public class JunctionProgressCar extends TestCase {
        @Override
        public void execute() {
            Junction loc = new Junction();
            Car c = new Car(loc);
            c.tick();
        }
    }

    public class JunctionProgressBus extends TestCase {
        @Override
        public void execute() {
            Junction loc = new Junction();
            Bus b = new Bus(loc);
            b.tick();
        }
    }

    public class JunctionProgressSnowplow extends TestCase {
        @Override
        public void execute() {
            Junction loc = new Junction();
            Snowplow sn = new Snowplow(loc);
            sn.tick();
        }
    }

    public class JunctionChoiceCar extends TestCase {
        @Override
        public void execute() {
            Junction loc = new Junction();

            Car c = new Car(loc);
            // TODO
        }
    }
}
