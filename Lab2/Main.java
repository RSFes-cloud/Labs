import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        
        Refrigerator fridge = new Refrigerator();
        Dishwasher dish = new Dishwasher();
        VacuumCleaner vac = new VacuumCleaner();

        
        System.out.println("Создано объектов Appliance: " + Appliance.getCreatedCount());

        
        System.out.print("Введите бренд холодильника: ");
        fridge.setBrand(sc.nextLine());
        System.out.print("Введите температуру холодильника: ");
        fridge.setTemperature(sc.nextInt());
        sc.nextLine(); // очистка строки

        
        Appliance[] devices = { fridge, dish, vac };

        
        devices[0].turnOn(30);

        
        devices[0].performMainFunction();

        
        System.out.println("\nИнформация об объектах:");
        for (Appliance a : devices) {
            System.out.println(a.getInfo());
        }

        sc.close();
    }
}