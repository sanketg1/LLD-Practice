package educative.ATMDesign;

public abstract class BankAccount {
    protected int accountNumber;
    protected double availableBalance;

    public BankAccount(int accountNumber, double availableBalance) {
        this.accountNumber = accountNumber;
        this.availableBalance = availableBalance;
    }

    public double getAvailableBalance() {
        return availableBalance;
    }

    public boolean withdraw(double amount){
        if (amount>0 && availableBalance>amount && amount<getWithdrawlLimit()){
            availableBalance-=amount;
            return true;
        }
        return false;
    }

    public boolean transfer( BankAccount toAccount, double amount){
        if (amount>0 && availableBalance>amount && amount<getWithdrawlLimit()){
            availableBalance-=amount;
            toAccount.availableBalance+=amount;
            return true;
        }
        return false;
    }
    public abstract double getWithdrawlLimit();

    public int getAccountNumber() {
        return accountNumber;
    }
}
