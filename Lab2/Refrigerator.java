public class Refrigerator extends KitchenAppliance {
    private int temperatureC;
    private boolean freezer;
    private int freezerVolumeLiters;

    public Refrigerator() {
        this("Unknown", 0, false, 0.0, 0, "N/A", 4, false, 0);
    }

    public Refrigerator(String brand, int powerWatts, boolean isOn,
                        double volumeLiters, int noiseDb, String energyClass,
                        int temperatureC, boolean freezer, int freezerVolumeLiters) {
        super(brand, powerWatts, isOn, volumeLiters, noiseDb, energyClass);
        this.temperatureC = temperatureC;
        this.freezer = freezer;
        this.freezerVolumeLiters = freezerVolumeLiters;
    }
// 2 метода поведения
    public void setTemperature(int temperatureC) {
        this.temperatureC = temperatureC;
    }

    public void showTemperature() {
        System.out.println("Температура: " + temperatureC + " °C");
    }

    
    @Override
    public void performMainFunction() {
        if (!isOn()) {
            System.out.println("Холодильник выключен, охлаждение невозможно.");
            return;
        }
        System.out.println("Холодильник охлаждает до " + temperatureC + " °C");
    }

    
    public int getTemperatureC() { return temperatureC; }
    public boolean hasFreezer() { return freezer; }
    public void setFreezer(boolean freezer) { this.freezer = freezer; }
    public int getFreezerVolumeLiters() { return freezerVolumeLiters; }
    public void setFreezerVolumeLiters(int v) { this.freezerVolumeLiters = v; }
}