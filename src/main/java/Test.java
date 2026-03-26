public class Test {
    public abstract class TestCase {
        public abstract String getName();

        public abstract void setup();

        public abstract void execute();
    }

    // 5.3.1
    public class JunctionProgressCar extends TestCase {
        private Junction loc;
        private Car c;

        @Override
        public String getName() {
            return "Autó haladása kereszteződésben/épületnél";
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

    // 5.3.2
    public class JunctionProgressBus extends TestCase {
        private Junction loc;
        private Bus b;

        @Override
        public String getName() {
            return "Busz haladása kereszteződésben/épületnél";
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

    // 5.3.3
    public class JunctionProgressSnowplow extends TestCase {
        private Junction loc;
        private Snowplow sn;

        @Override
        public String getName() {
            return "Hókotró haladása kereszteződésben/épületnél";
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

    // 5.3.4
    public class JunctionChoiceCar extends TestCase {
        private CarPlayer p;
        private Lane dest1;
        private Lane dest2;

        @Override
        public String getName() {
            return "Autó döntéshozása kereszteződésben";
        }

        @Override
        public void setup() {
            Junction j = new Junction();
            Building home = new Building(j);
            Building work = new Building(null);
            p = new CarPlayer(home, work);
            p.getCar().setLocation(j);
            dest1 = new Lane(j, new Junction());
            dest2 = new Lane(j, new Junction());
        }

        @Override
        public void execute() {
            int choice = Skeleton.askInt("Which lane? 0 or 1");
            p.choseDirection(choice == 0 ? dest1 : dest2, 0);
        }
    }

    // 5.3.5
    public class JunctionChoiceBus extends TestCase {
        private BusPlayer p;
        private Lane dest1;
        private Lane dest2;

        @Override
        public String getName() {
            return "Busz döntéshozása kereszteződésben";
        }

        @Override
        public void setup() {
            Junction j = new Junction();
            p = new BusPlayer(j);
            dest1 = new Lane(j, new Junction());
            dest2 = new Lane(j, new Junction());
        }

        @Override
        public void execute() {
            int choice = Skeleton.askInt("Which lane? 0 or 1");
            p.choseDirection(choice == 0 ? dest1 : dest2, 0);
        }
    }

    // 5.3.6
    public class JunctionChoiceSnowplow extends TestCase {
        private SnowplowPlayer p;
        private Lane dest1;
        private Lane dest2;

        @Override
        public String getName() {
            return "Hókotró döntéshozása kereszteződésben";
        }

        @Override
        public void setup() {
            Junction j = new Junction();
            p = new SnowplowPlayer(j);
            dest1 = new Lane(j, new Junction());
            dest2 = new Lane(j, new Junction());
        }

        @Override
        public void execute() {
            int choice = Skeleton.askInt("Which lane? 0 or 1");
            p.choseDirection(choice == 0 ? dest1 : dest2, 0);
        }
    }
}
