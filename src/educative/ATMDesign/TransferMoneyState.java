package educative.ATMDesign;

public class TransferMoneyState extends ATMState {

    @Override
    public void transferMoney(ATM atm, ATMCard card, BankAccount toAccount, double amount){
        BankAccount fromAccount = atm.getActiveUser().getBankAccount();
        double accLimit = fromAccount.getWithdrawalLimit();

        if(amount<=accLimit && amount<=fromAccount.getAvailableBalance()){
            boolean transferred = fromAccount.transfer(toAccount, amount);
            if (transferred) {
                atm.getPrinter().printReceipt("Transferred: $" + amount +
                        " from Acc#"+fromAccount.getAccountNumber() + " to Acc#"+toAccount.getAccountNumber() +
                        " | Your balance: $" + fromAccount.getAvailableBalance());
                atm.getScreen().showMessage("Transfer successful.");
            } else {
                atm.getScreen().showMessage("Transfer failed.");
            }
        }else{
            atm.getScreen().showMessage("Transfer denied. Check your balance or limits.");
        }

        // Return to selection state
        atm.setAtmStatus(ATMStatus.SelectionOption);
        atm.setCurrentATMState(new SelectionOptionState());
    }
}
