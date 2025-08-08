package educative.ATMDesign;

public class SavingAccount extends BankAccount {
    private static final double WITHDRAWAL_LIMIT = 1000.0; // Example limit for savings account

    public SavingAccount(int accountNumber, double availableBalance) {
        super(accountNumber, availableBalance);
    }

    @Override
    public double getWithdrawalLimit() {
        return WITHDRAWAL_LIMIT;
    }

    @Override
    public String toString() {
        return "SavingAccount{" +
                "accountNumber=" + accountNumber +
                ", availableBalance=" + availableBalance +
                '}';
    }
}
