public class Dishwasher extends KitchenAppliance {
    private int programs;
    private int waterLitersPerCycle;
    private boolean drying;

    public Dishwasher() {
        this("Unknown", 0, false, 0.0, 0, "N/A", 0, 0, false);
    }

    public Dishwasher(String brand, int powerWatts, boolean isOn,
                      double volumeLiters, int noiseDb, String energyClass,
                      int programs, int waterLitersPerCycle, boolean drying) {
        super(brand, powerWatts, isOn, volumeLiters, noiseDb, energyClass);
        this.programs = programs;
        this.waterLitersPerCycle = waterLitersPerCycle;
        this.drying = drying;
    }

    
    public void startWash() {
        if (!isOn()) {
            System.out.println("Посудомойка выключена, запуск невозможен.");
            return;
        }
        System.out.println("Мойка запущена. Вода/цикл: " + waterLitersPerCycle + " л");
    }

    public void startDrying() {
        if (!isOn()) {
            System.out.println("Посудомойка выключена, сушка невозможна.");
            return;
        }
        if (!drying) {
            System.out.println("Сушка не поддерживается.");
            return;
        }
        System.out.println("Сушка запущена.");
    }

    
    @Override
    public void performMainFunction() {
        startWash();
    }

    public int getPrograms() { return programs; }
    public void setPrograms(int programs) { this.programs = programs; }

    public int getWaterLitersPerCycle() { return waterLitersPerCycle; }
    public void setWaterLitersPerCycle(int w) { this.waterLitersPerCycle = w; }

    public boolean hasDrying() { return drying; }
    public void setDrying(boolean drying) { this.drying = drying; }
}