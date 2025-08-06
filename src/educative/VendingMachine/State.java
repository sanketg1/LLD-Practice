package educative.VendingMachine;

public interface State {
    void insertMoney(VendingMachine machine, double amount);
    void selectProduct(VendingMachine machine, int rackNumber);
}