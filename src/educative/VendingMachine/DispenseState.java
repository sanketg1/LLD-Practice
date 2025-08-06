package educative.VendingMachine;

public class DispenseState implements State {
    @Override
    public void insertMoney(VendingMachine m, double amt) {
        System.out.println("[DispenseState] insertMoney: Currently dispensing. Please wait.");
    }

    @Override
    public void selectProduct(VendingMachine m, int rackNo) {
        System.out.println("[DispenseState] selectProduct: Already dispensing. Please wait.");
    }
}