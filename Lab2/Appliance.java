public abstract class Appliance {
    private String brand;
    private int powerWatts;
    private boolean isOn;

    private static int createdCount = 0;

    public Appliance() {
        this("Unknown", 0, false);
    }

    public Appliance(String brand, int powerWatts, boolean isOn) {
        this.brand = brand;
        this.powerWatts = powerWatts;
        this.isOn = isOn;
        createdCount++;
    }

  
    public void turnOn() {
        isOn = true;
    }

 
    public void turnOn(int minutes) {
        isOn = true;
        System.out.println("Включено на " + minutes + " минут(ы)");
    }

    public void turnOff() {
        isOn = false;
    }


    public abstract void performMainFunction();

  
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getPowerWatts() { return powerWatts; }
    public void setPowerWatts(int powerWatts) { this.powerWatts = powerWatts; }

    public boolean isOn() { return isOn; }
    public void setOn(boolean on) { isOn = on; }

    public static int getCreatedCount() {
        return createdCount;
    }
	
    public String getInfo() {
        return getClass().getSimpleName() +
                " {brand='" + brand + "', powerWatts=" + powerWatts + ", isOn=" + isOn + "}";
    }
}