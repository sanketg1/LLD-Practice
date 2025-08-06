package educative.VendingMachine;

import java.util.*;

public class VendingMachine {
    private final State noMoneyState          = new NoMoneyState();
    private final State moneyInsertedState   = new MoneyInsertedState();
    private final State dispenseState        = new DispenseState();

    private State currentState = noMoneyState;
    private double currentAmount = 0.0;
    private int selectedRack     = -1;
    private final Inventory inventory = new Inventory();
    private static VendingMachine instance;

    private VendingMachine() {}
    public static VendingMachine getInstance() {
        if (instance == null) instance = new VendingMachine();
        return instance;
    }

    // State accessors
    State getNoMoneyState()         { return noMoneyState; }
    State getMoneyInsertedState()   { return moneyInsertedState; }
    State getDispenseState()        { return dispenseState; }

    // State transition with logging
    void setState(State s) {
        System.out.printf(">> Transition: %s -> %s%n",
                currentState.getClass().getSimpleName(), s.getClass().getSimpleName());
        this.currentState = s;
    }

    // Context getters/setters
    double getCurrentAmount()                 { return currentAmount; }
    void addToCurrentAmount(double amt)       { currentAmount += amt; }
    Inventory getInventory()                 { return inventory; }
    void setSelectedRack(int r)               { selectedRack = r; }

    // User operations
    public void insertMoney(double amt)       { currentState.insertMoney(this, amt); }
    public void selectProduct(int rackNo)     { currentState.selectProduct(this, rackNo); }
    public void dispenseProduct() {
        System.out.println("[DispenseState] dispenseProduct: Dispensing now...");
        Rack rack = inventory.getRack(selectedRack);
        Product p = rack.peekProduct();
        System.out.printf("Dispensing %s. Enjoy!%n", p.getName());
        rack.dispenseOne();
        double change = currentAmount - p.getPrice();
        if (change > 0) System.out.printf("Returning change: $%.2f%n", change);
        reset();
    }
    public void refund() {
        System.out.printf("Refunding full amount: $%.2f%n", currentAmount);
        reset();
    }

    private void reset() {
        currentAmount = 0;
        selectedRack  = -1;
        setState(noMoneyState);
    }

    // Admin operations
    public void addRack(Rack rack)             { inventory.addRack(rack); }
    public void loadProduct(int rackNo, Product product, int qty) {
        Rack r = inventory.getRack(rackNo);
        if (r == null) {
            System.out.println("[Admin] No such rack: " + rackNo);
            return;
        }
        r.loadProduct(product, qty);
        System.out.printf("[Admin] Loaded %d × %s into rack %d%n", qty, product.getName(), rackNo);
    }

    public void showInventory() {
        System.out.println("\n=== Inventory Status ===");
        for (Rack r : inventory.allRacks()) System.out.println(r);
        System.out.println("========================\n");
    }
}