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
            c = new Car(null);
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

    //5.3.7
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
            c = new Car(null);
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

    //5.3.8
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
            c = new Car(null);
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

    //5.3.9
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
            c = new Car(null);
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

    //5.3.10
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
            c = new Car(null);
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

    //5.3.11
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

    //5.3.12
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

    //5.3.13
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
            c = new Car(null);
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

    //5.3.14
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
            c = new Car(null);
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

    //5.3.15
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

    //5.3.16
    public class SnowingOnSmallSnowWithNoSalt extends TestCase
    {
        private Lane l;
        private SmallSnow surf;

        @Override
        public String getName() {
            return "SnowingOnSmallSnowWithNoSalt";
        }

        @Override
        public void setup() {
            l = new Lane(null,null);
            surf = new SmallSnow(l);
            l.setSurface(surf);
        }

        @Override
        public void execute() {
            l.tick();
        }
    }
    //5.3.17
    public class SnowingOnSmallSnowWithSalt extends TestCase
    {
        private Lane l;
        private SmallSnow surf;

        @Override
        public String getName() {
            return "SnowingOnSmallSnowWithSalt";
        }

        @Override
        public void setup() {
            l = new Lane(null,null);
            surf = new SmallSnow(l);
            l.setSurface(surf);
            l.salt();
        }

        @Override
        public void execute() {
            l.tick();
        }
    }
    //5.3.18
    public class SnowingNotOnSmallSnowWithNoSalt extends TestCase
    {
        private Lane l;
        private DeepSnow surf;

        @Override
        public String getName() {
            return "SnowingNotOnSmallSnowWithNoSalt";
        }

        @Override
        public void setup() {
            l = new Lane(null,null);
            surf = new DeepSnow(l);
            l.setSurface(surf);
        }

        @Override
        public void execute() {
            l.tick();
        }
    }
    //5.3.19
    public class SnowingOnIceWithSalt extends TestCase
    {
        private Lane l;
        private Ice surf;

        @Override
        public String getName() {
            return "SnowingOnIceWithSalt";
        }

        @Override
        public void setup() {
            l = new Lane(null,null);
            surf = new Ice(l);
            l.setSurface(surf);
            l.salt();
        }

        @Override
        public void execute() {
            l.tick();
        }
    }
    //5.3.20
    public class SnowingOnDeepSnowWithSalt extends TestCase
    {
        private Lane l;
        private DeepSnow surf;

        @Override
        public String getName() {
            return "SnowingOnDeepSnowWithSalt";
        }

        @Override
        public void setup() {
            l = new Lane(null, null);
            surf = new DeepSnow(l);
            l.setSurface(surf);
            l.salt();
        }

        @Override
        public void execute() {
            l.tick();
        }
    }

    //5.3.21
    public class AddingIceNotToSmallSnow extends TestCase
    {
        private Car c;
        private Lane l;
        private Ice surf;

        @Override
        public String getName() {
            return "AddingIceNotToSmallSnow";
        }

        @Override
        public void setup() {
            l = new Lane(null, null);
            surf = new Ice(l);
            c = new Car(null);
            c.setLocation(l);
            l.setSurface(surf);
        }

        @Override
        public void execute() {
            l.remove(c);
        }
    }
    //5.3.22
    public class AddingIceToSmallSnow extends TestCase
    {
        private Car c;
        private Lane l;
        private SmallSnow surf;

        @Override
        public String getName() {
            return "AddingIceToSmallSnow";
        }

        @Override
        public void setup() {
            l = new Lane(null, null);
            surf = new SmallSnow(l);
            l.setSurface(surf);
            c = new Car(null);
            c.setLocation(l);
        }

        @Override
        public void execute() {
            l.remove(c);
        }
    }

    //5.3.23
    public class CarRecovery extends TestCase
    {
        private Building home;
        private CarPlayer owner;
        private Car c;
        private Lane loc;
        private Recoverer rec;

        @Override
        public String getName() {
            return "CarRecovery";
        }

        @Override
        public void setup() {
            loc = new Lane(null, null);
            loc.setSurface(new SmallSnow(loc));
            home = new Building(new Junction());
            owner = new CarPlayer(home, null);
            c = owner.getCar();
            rec = new Recoverer();
            rec.addToRecoveryQueue(c);
            c.setLocation(loc);
        }

        @Override
        public void execute() {
            rec.tick();
        }
    }

    //5.3.24
    public class BusTriesRecoveringItself extends TestCase
    {
        private Bus b;
        private Lane loc;

        @Override
        public String getName() {
            return "BusTriesRecoveringItself";
        }

        @Override
        public void setup() {
            loc = new Lane(null, null);
            b = new Bus();
            b.setLocation(loc);
            b.crash();
        }

        @Override
        public void execute() {
            b.tick();    
        }
    }
    
    //5.3.25
    public class ChangeEquipment extends TestCase {
        private SnowplowPlayer p;
        private Snowplow s;
        private Sweeper e1;
        private IceBreaker e2;
        private Warehouse w;
        private Junction j;

        @Override
        public String getName() {
            return "Hókotró felszerelésének megváltoztatása";
        }

        @Override
        public void setup() {
            j = new Junction();
            p = new SnowplowPlayer(j);
            s = p.getSnowplow(0);
            e1 = new Sweeper(p);
            e2 = new IceBreaker(p);
            w = new Warehouse(j);
            s.setEquipment(e1);
        }

        @Override
        public void execute() {
            w.changeEquipment(s, e2);
        }
    }

    // 5.3.33
    public class BuyEquipment extends TestCase {
        private Bank b;
        private Warehouse w;
        private SnowplowPlayer p;

        @Override
        public String getName() {
            return "Kotrófej vásárlás";
        }

        @Override
        public void setup() {
            b = new Bank();
            w = new Warehouse(new Junction());
            p = new SnowplowPlayer(new Junction());

            w.setBank(b);
            b.setWarehouse(w);
            p.setWarehouse(w);
        }

        @Override
        public void execute() {
            int id = Skeleton.askInt("Milyen ID-ju eszkozt vasaroljon? ");
            p.buyEquipment(id);
        }
    }

    // 5.3.34
    public class BuySnowplow extends TestCase {
        private Bank b;
        private Warehouse w;
        private SnowplowPlayer p;

        @Override
        public String getName() {
            return "Új hókotró vásárlása";
        }

        @Override
        public void setup() {
            b = new Bank();
            w = new Warehouse(new Junction());
            p = new SnowplowPlayer(new Junction());

            w.setBank(b);
            b.setWarehouse(w);
            p.setWarehouse(w);
        }

        @Override
        public void execute() {
            p.buySnowplow();
        }
    }

    // 5.3.35
    public class RewardForClearing extends TestCase {
        private Bank b;
        private RoadNetwork rn;
        private Lane l;
        private SnowplowPlayer p;

        @Override
        public String getName() {
            return "Takarításért járó jutalom jóváírása";
        }

        @Override
        public void setup() {
            b = new Bank();

            rn = new RoadNetwork();
            rn.setBank(b);

            l = new Lane(null, null);
            l.setRoadNetwork(rn);

            p = new SnowplowPlayer(new Junction());
        }

        @Override
        public void execute() {
            l.cleared(p);
        }
    }

    // 5.3.36
    public class BusRoundComplete extends TestCase {
        private Bank b;
        private RoadNetwork rn;
        private Junction j;
        private Building bs;
        private BusPlayer bp;
        private Bus bus;

        @Override
        public String getName() {
            return "Buszforduló teljesítése utáni jutalom";
        }

        @Override
        public void setup() {
            b = new Bank();
            rn = new RoadNetwork();
            rn.setBank(b);

            j = new Junction();
            j.setRoadNetwork(rn);

            bs = new Building(j);
            j.addBuilding(bs);

            bp = new BusPlayer(j);

            bus = new Bus();
            bus.setLocation(j);
            bus.setOwner(bp);
            bp.setBus(bus);
        }

        @Override
        public void execute() {
            j.arrived(bus);
        }
    }
}
