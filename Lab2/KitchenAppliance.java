public class KitchenAppliance extends Appliance {
    private double volumeLiters;
    private int noiseDb;
    private String energyClass;

    public KitchenAppliance() {
        this("Unknown", 0, false, 0.0, 0, "N/A");
    }

    public KitchenAppliance(String brand, int powerWatts, boolean isOn,
                            double volumeLiters, int noiseDb, String energyClass) {
        super(brand, powerWatts, isOn);
        this.volumeLiters = volumeLiters;
        this.noiseDb = noiseDb;
        this.energyClass = energyClass;
    }

    public void showNoise() {
        System.out.println("Шум: " + noiseDb + " дБ");
    }

    public void showEnergyClass() {
        System.out.println("Класс энергопотребления: " + energyClass);
    }

   
    @Override
    public void performMainFunction() {
        System.out.println("Кухонная техника выполняет базовую функцию.");
    }

    
    public double getVolumeLiters() { return volumeLiters; }
    public void setVolumeLiters(double volumeLiters) { this.volumeLiters = volumeLiters; }

    public int getNoiseDb() { return noiseDb; }
    public void setNoiseDb(int noiseDb) { this.noiseDb = noiseDb; }

    public String getEnergyClass() { return energyClass; }
    public void setEnergyClass(String energyClass) { this.energyClass = energyClass; }
}