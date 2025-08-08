package educative.ATMDesign;

public class BalanceInquiryState extends  ATMState {

    @Override
    public void displayBalance(ATM atm,ATMCard card){
        atm.getScreen().showMessage("Your current balance is: " + atm.getActiveUser().getBankAccount().getAvailableBalance());
        atm.getPrinter().printReceipt("Balance :"+ atm.getActiveUser().getBankAccount().getAvailableBalance());
        atm.setCurrentATMState(new SelectionOptionState());
        atm.setAtmStatus(ATMStatus.SelectionOption);
    }


}
