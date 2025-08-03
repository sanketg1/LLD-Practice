package educative.AmazonLockerService;

public class Customer {
    private String customerId;
    private String name;
    private String email;
    private String phone;

    public Customer(String customerId, String name, String email, String phone) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
    public String getCustomerId(){
        return customerId;
    }
    public void placeOrder(Order order){
        System.out.println("Customer" + customerId+ "Placed Order"+ order.getOrderId());
    }
    public void requestReturn(Order order){
        System.out.println("Customer "+customerId + " requested return for order "+ order.getOrderId());
    }

    public void receiveNotification(Notification notification){
        System.out.println("Customer "+customerId + " receive notification for order "+ notification.getOrderId());
    }
}
