package educative.VendingMachine;

public class Product {
    private final String name;
    private final int id;
    private final double price;
    private final ProductType type;

    public Product(int id, String name, double price, ProductType type) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public int getId()            { return id; }
    public String getName()       { return name; }
    public double getPrice()      { return price; }
    public ProductType getType()  { return type; }

    @Override
    public String toString() {
        return String.format("%s (id=%d) @ $%.2f", name, id, price);
    }
}
