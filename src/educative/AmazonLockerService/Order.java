package educative.AmazonLockerService;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId ;
    private List<Item>  items;
    private String deliveryLocation ;
    private String customerId;

    public Order( String orderId, String deliveryLocation, String customerId) {
        this.items = new ArrayList<>();
        this.orderId = orderId;
        this.deliveryLocation = deliveryLocation;
        this.customerId = customerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void addItem(Item item){
        items.add(item);
    }
}
