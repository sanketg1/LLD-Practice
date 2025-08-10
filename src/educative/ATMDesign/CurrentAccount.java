package educative.ATMDesign;

public class CurrentAccount extends BankAccount {
    public CurrentAccount(int accountNumber, double availableBalance) {
        super(accountNumber, availableBalance);
    }

    @Override
    public double getWithdrawlLimit() {
        return 5000.0;
    }
}
