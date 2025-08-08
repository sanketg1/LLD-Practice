package educative.ATMDesign;

public class ATMCard {

    private String cardNumber;;
    private int pin;
    private String customerName;
    private  String cardExpiryDate;


    public ATMCard(String cardNumber, String customerName, String expiry, int pin) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.cardExpiryDate = expiry;
        this.pin = pin;
    }

    public boolean validatePin(int pin) {
        return this.pin == pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }
}
