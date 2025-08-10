package educative.ATMDesign;

public class SavingAccount extends BankAccount {

    public SavingAccount(int accountNumber, double availableBalance) {
        super(accountNumber, availableBalance);
    }

    @Override
    public double getWithdrawlLimit() {
        return 1000.0;
    }
}
