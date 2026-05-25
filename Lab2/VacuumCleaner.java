public class VacuumCleaner extends Appliance {
    private int suctionPowerPa;
    private double dustbinLiters;
    private boolean hepa;

    public VacuumCleaner() {
        this("Unknown", 0, false, 0, 0.0, false);
    }

    public VacuumCleaner(String brand, int powerWatts, boolean isOn,
                         int suctionPowerPa, double dustbinLiters, boolean hepa) {
        super(brand, powerWatts, isOn);
        this.suctionPowerPa = suctionPowerPa;
        this.dustbinLiters = dustbinLiters;
        this.hepa = hepa;
    }

    
    public void vacuum() {
        if (!isOn()) {
            System.out.println("Пылесос выключен, уборка невозможна.");
            return;
        }
        System.out.println("Пылесосит (тяга: " + suctionPowerPa + " Па)");
    }

    public void emptyDustbin() {
        dustbinLiters = 0.0;
        System.out.println("Контейнер очищен.");
    }

    
    @Override
    public void performMainFunction() {
        vacuum();
    }

    
    public int getSuctionPowerPa() { return suctionPowerPa; }
    public void setSuctionPowerPa(int s) { this.suctionPowerPa = s; }

    public double getDustbinLiters() { return dustbinLiters; }
    public void setDustbinLiters(double d) { this.dustbinLiters = d; }

    public boolean hasHepa() { return hepa; }
    public void setHepa(boolean hepa) { this.hepa = hepa; }
}