public class Product {
    private String name;
    private double price;

    private static int count;

    {
        count++;
    }

    public Product() {
        // Пустий конструктор без параметрів
    }

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public static int count() {
        return count;
    }
}


