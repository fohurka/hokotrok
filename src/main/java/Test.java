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
            c = new Car();
            c.setLocation(loc);
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
            b = new Bus();
            b.setLocation(loc);
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
            sn = new Snowplow();
            sn.setLocation(loc);
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

    public class CarProgressSmallSnow extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;

        @java.lang.Override
        public String getName() {
            return "CarProgressSmallSnow";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new SmallSnow(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
        }

        public void execute() {
            c.tick();
        }
    }

    public class CarProgressDeepSnowStuck extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Lane rightNeighbor;
        private Surface rightNeighborSurf;

        @java.lang.Override
        public String getName() {
            return "CarProgressDeepSnowStuck";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new DeepSnow(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
            rightNeighbor = new Lane(null, end);
            rightNeighborSurf = new DeepSnow(rightNeighbor);
            rightNeighbor.setSurface(rightNeighborSurf);
            loc.addNeighbors(null, rightNeighbor);
        }

        public void execute() {
            c.tick();
        }
    }

    public class CarProgressDeepSnowSwitch extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Lane rightNeighbor;
        private Surface rightNeighborSurf;

        @java.lang.Override
        public String getName() {
            return "CarProgressDeepSnowSwitch";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new DeepSnow(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
            rightNeighbor = new Lane(null, end);
            rightNeighborSurf = new SmallSnow(rightNeighbor);
            rightNeighbor.setSurface(rightNeighborSurf);
            loc.addNeighbors(null, rightNeighbor);
        }

        public void execute() {
            c.tick();
        }
    }

    public class CarProgressIce extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Bus bus;
        private Recoverer rec;

        @java.lang.Override
        public String getName() {
            return "CarProgressIce";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new Ice(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
            bus = new Bus();
            bus.setLocation(loc);
            rec = new Recoverer();
            c.setRecoverer(rec);
        }

        public void execute() {
            c.tick();
        }
    }

    public class BusProgressSmallSnow extends TestCase {
        private Bus b;
        private Junction end;
        private Lane loc;
        private Surface surf;

        @java.lang.Override
        public String getName() {
            return "BusProgressSmallSnow";
        }

        @java.lang.Override
        public void setup() {
            b = new Bus();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new SmallSnow(loc);
            loc.setSurface(surf);
            b.setLocation(loc);
            end.addEnding(loc);
        }

        public void execute() {
            b.tick();
        }
    }

    public class BusProgressDeepSnow extends TestCase {
        private Bus b;
        private Junction end;
        private Lane loc;
        private Surface surf;

        @java.lang.Override
        public String getName() {
            return "BusProgressDeepSnow";
        }

        @java.lang.Override
        public void setup() {
            b = new Bus();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new DeepSnow(loc);
            loc.setSurface(surf);
            b.setLocation(loc);
            end.addEnding(loc);
        }

        public void execute() {
            b.tick();
        }
    }

    public class BusProgressIce extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Bus bus;
        private Recoverer rec;

        @java.lang.Override
        public String getName() {
            return "BusProgressIce";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new Ice(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
            bus = new Bus();
            bus.setLocation(loc);
            rec = new Recoverer();
            c.setRecoverer(rec);
        }

        public void execute() {
            bus.tick();
        }
    }

    public class SnowplowProgressDeepSnow extends TestCase {
        private Car c;
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Snowplow sn;
        private Equipment eq;

        @java.lang.Override
        public String getName() {
            return "SnowplowProgressDeepSnow";
        }

        @java.lang.Override
        public void setup() {
            c = new Car();
            end = new Junction();
            loc = new Lane(null, end);
            surf = new DeepSnow(loc);
            loc.setSurface(surf);
            c.setLocation(loc);
            end.addEnding(loc);
            sn = new Snowplow();
            sn.setLocation(loc);
            eq = new Sweeper();
            sn.setEquipment(eq);
        }

        public void execute() {
            sn.tick();
        }
    }

    public class SnowplowProgressNonDeepSnow extends TestCase {
        private Junction end;
        private Lane loc;
        private Surface surf;
        private Snowplow sn;
        private Equipment eq;

        @java.lang.Override
        public String getName() {
            return "SnowplowProgressNonDeepSnow";
        }

        @java.lang.Override
        public void setup() {
            end = new Junction();
            loc = new Lane(null, end);
            surf = new SmallSnow(loc);
            loc.setSurface(surf);
            end.addEnding(loc);
            sn = new Snowplow();
            sn.setLocation(loc);
            eq = new Sweeper();
            sn.setEquipment(eq);
        }

        public void execute() {
            sn.tick();
        }
    }
}
