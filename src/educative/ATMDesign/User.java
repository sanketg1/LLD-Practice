package educative.ATMDesign;

public class User {
    private ATMCard card;
    private BankAccount bankAccount;

    public User(ATMCard atmCard, BankAccount bankAccount) {
        this.card = atmCard;
        this.bankAccount = bankAccount;
    }

    public ATMCard getCard() {
        return card;
    }

    public BankAccount getBankAccount() {
        return bankAccount;
    }
}
