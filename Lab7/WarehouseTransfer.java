import java.util.ArrayList;

public class WarehouseTransfer {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse();

        warehouse.addProduct(new Product("Товар 1", 40));
        warehouse.addProduct(new Product("Товар 2", 50));
        warehouse.addProduct(new Product("Товар 3", 60));
        warehouse.addProduct(new Product("Товар 4", 30));
        warehouse.addProduct(new Product("Товар 5", 70));
        warehouse.addProduct(new Product("Товар 6", 20));

        Loader loader1 = new Loader("Грузчик 1", warehouse);
        Loader loader2 = new Loader("Грузчик 2", warehouse);
        Loader loader3 = new Loader("Грузчик 3", warehouse);

        loader1.start();
        loader2.start();
        loader3.start();
    }
}

class Product {
    private final String name;
    private final int weight;

    public Product(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}

class Warehouse {
    private final ArrayList<Product> products = new ArrayList<>();
    private int currentWeight = 0;

    public synchronized void addProduct(Product product) {
        products.add(product);
    }

    public synchronized Product takeProduct() {
        if (products.isEmpty()) {
            return null;
        }

        Product product = products.get(0);

        if (currentWeight + product.getWeight() > 150) {
            System.out.println("Собрано " + currentWeight + " кг. Грузчики отправляются на другой склад.");
            currentWeight = 0;
        }

        currentWeight += product.getWeight();
        products.remove(0);

        if (currentWeight == 150) {
            System.out.println("Собрано 150 кг. Грузчики отправляются на другой склад.");
            currentWeight = 0;
        }

        return product;
    }
}

class Loader extends Thread {
    private final Warehouse warehouse;

    public Loader(String name, Warehouse warehouse) {
        super(name);
        this.warehouse = warehouse;
    }

    public void run() {
        Product product = warehouse.takeProduct();

        while (product != null) {
            System.out.println(getName() + " переносит " + product.getName() + " весом " + product.getWeight() + " кг.");
            product = warehouse.takeProduct();
        }
    }
}