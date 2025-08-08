package educative.ATMDesign;

public class CurrentAccount extends BankAccount {

    private static final double WITHDRAWAL_LIMIT = 5000.0; // Example limit for current account

    public CurrentAccount(int accountNumber, double availableBalance) {
        super(accountNumber, availableBalance);
    }

    @Override
    public double getWithdrawalLimit() {
        return WITHDRAWAL_LIMIT;
    }

    @Override
    public String toString() {
        return "CurrentAccount{" +
                "accountNumber=" + accountNumber +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
