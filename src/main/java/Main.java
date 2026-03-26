import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Test test = new Test();
        List<Test.TestCase> tests = new ArrayList<>();

        tests.add(test.new JunctionProgressCar());
        tests.add(test.new JunctionProgressBus());
        tests.add(test.new JunctionProgressSnowplow());
        tests.add(test.new JunctionChoiceCar());
        tests.add(test.new JunctionChoiceBus());
        tests.add(test.new JunctionChoiceSnowplow());
        tests.add(test.new CarProgressSmallSnow());
        tests.add(test.new CarProgressDeepSnowStuck());
        tests.add(test.new CarProgressDeepSnowSwitch());
        tests.add(test.new CarProgressIce());
        tests.add(test.new BusProgressSmallSnow());
        tests.add(test.new BusProgressDeepSnow());
        tests.add(test.new BusProgressIce());
        tests.add(test.new SnowplowProgressDeepSnow());
        tests.add(test.new SnowplowProgressNonDeepSnow());

        while (true) {
            System.out.println("\n=== Menü ===");
            System.out.println("0. Kilépés");
            for (int i = 0; i < tests.size(); i++) {
                System.out.println((i + 1) + ". " + tests.get(i).getName());
            }

            int choice = Skeleton.askInt("Válassz egy tesztesetet a sorszámaq alapján: ");

            if (choice == 0) {
                System.out.println("Kilépés...");
                break;
            }

            if (choice > 0 && choice <= tests.size()) {
                Test.TestCase selectedTest = tests.get(choice - 1);
                System.out.println("\n--- [" + choice + "] " + selectedTest.getName() + " ---");
                System.out.println(">>> Teszt inicializálása (setup)...");
                selectedTest.setup();
                System.out.println(">>> Teszt futtatása (execute)...");
                selectedTest.execute();
                System.out.println("-------------------------------------");
            } else {
                System.out.println("Érvénytelen sorszám! Kérlek a listából válassz.");
            }
        }
    }
}
