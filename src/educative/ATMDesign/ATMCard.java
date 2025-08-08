package educative.ATMDesign;

public class ATMCard {
    private String cardNumber;
    private String customerName;
    private String cardExpiryDate;
    private int pin;

    public ATMCard(String cardNumber, String customerName, String cardExpiryDate, int pin) {
        this.cardNumber = cardNumber;
        this.customerName = customerName;
        this.cardExpiryDate = cardExpiryDate;
        this.pin = pin;
    }

    public boolean validatePin(int enteredPin){
        return this.pin==enteredPin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
