package educative.VendingMachine;

public class NoMoneyState implements State {
    @Override
    public void insertMoney(VendingMachine m, double amt) {
        System.out.printf("[NoMoneyState] insertMoney: received $%.2f%n", amt);
        m.addToCurrentAmount(amt);
        System.out.printf("[NoMoneyState] Current amount = $%.2f. You may now select a product.%n", m.getCurrentAmount());
        m.setState(m.getMoneyInsertedState());
    }

    @Override
    public void selectProduct(VendingMachine m, int rackNo) {
        System.out.println("[NoMoneyState] selectProduct: No money inserted. Please insert cash first.");
    }
}
