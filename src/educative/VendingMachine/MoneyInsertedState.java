package educative.VendingMachine;

public class MoneyInsertedState implements State {
    @Override
    public void insertMoney(VendingMachine m, double amt) {
        System.out.printf("[MoneyInsertedState] insertMoney: adding $%.2f to current amount.%n", amt);
        m.addToCurrentAmount(amt);
        System.out.printf("[MoneyInsertedState] Total amount = $%.2f.%n", m.getCurrentAmount());
    }

    @Override
    public void selectProduct(VendingMachine m, int rackNo) {
        System.out.printf("[MoneyInsertedState] selectProduct: rack %d selected.%n", rackNo);
        Rack rack = m.getInventory().getRack(rackNo);
        if (rack == null || rack.isEmpty()) {
            System.out.println("[MoneyInsertedState] Selected rack is empty or does not exist. Refunding...");
            m.refund();
            return;
        }
        Product p = rack.peekProduct();
        double price = p.getPrice();
        double paid  = m.getCurrentAmount();
        System.out.printf("[MoneyInsertedState] Product price = $%.2f, paid = $%.2f.%n", price, paid);
        if (paid < price) {
            System.out.println("[MoneyInsertedState] Insufficient funds. Refunding...");
            m.refund();
            return;
        }
        System.out.println("[MoneyInsertedState] Sufficient funds. Proceeding to dispense.");
        m.setSelectedRack(rackNo);
        m.setState(m.getDispenseState());
        m.dispenseProduct();
    }
}
