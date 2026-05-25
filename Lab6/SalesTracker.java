import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return name + " (" + price + " руб.)";
    }
}

public class SalesTracker {

    private ArrayList<Product> soldProducts;

    public SalesTracker() {
        soldProducts = new ArrayList<>();
    }


    public void addSale(Product product) {
        soldProducts.add(product);
        System.out.println("Товар продан: " + product.getName());
    }


    public void printSoldProducts() {
        System.out.println("\n--- Список проданных товаров ---");
        for (Product p : soldProducts) {
            System.out.println("- " + p.toString());
        }
    }


    public double calculateTotalSales() {
        double total = 0;
        for (Product p : soldProducts) {
            total += p.getPrice();
        }
        return total;
    }


    public String getMostPopularProduct() {
        if (soldProducts.isEmpty()) {
            return "Нет данных о продажах";
        }

        Map<String, Integer> counts = new HashMap<>();
        for (Product p : soldProducts) {
            counts.put(p.getName(), counts.getOrDefault(p.getName(), 0) + 1);
        }

        String popularProduct = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                popularProduct = entry.getKey();
            }
        }

        return popularProduct;
    }

    public static void main(String[] args) {
        SalesTracker tracker = new SalesTracker();

        // Создаем несколько товаров
        Product laptop = new Product("Ноутбук", 60000);
        Product mouse = new Product("Мышь", 1500);
        Product keyboard = new Product("Клавиатура", 3000);

        // Имитируем продажи
        tracker.addSale(laptop);
        tracker.addSale(mouse);
        tracker.addSale(mouse);
        tracker.addSale(keyboard);
        tracker.addSale(mouse);

        // Выводим результаты
        tracker.printSoldProducts();
        System.out.println("\nОбщая сумма продаж: " + tracker.calculateTotalSales() + " руб.");
        System.out.println("Наиболее популярный товар: " + tracker.getMostPopularProduct());
    }
}