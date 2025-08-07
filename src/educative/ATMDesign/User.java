package educative.ATMDesign;

public class User {
    private ATMCard atmCard;
    private BankAccount bankAccount;

    public User(ATMCard atmCard, BankAccount bankAccount) {
        this.atmCard = atmCard;
        this.bankAccount = bankAccount;
    }

    public ATMCard getAtmCard() {
        return atmCard;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
