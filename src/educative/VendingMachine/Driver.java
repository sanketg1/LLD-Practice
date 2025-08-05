package educative.VendingMachine;

public class Driver {
    public static void main(String[] args) {
        VendingMachine vm = VendingMachine.getInstance();

        // Setup
        vm.addRack(new Rack(1)); vm.addRack(new Rack(2)); vm.addRack(new Rack(3));
        Product choc = new Product(101, "Chocolate Bar", 1.50, ProductType.CHOCOLATE);
        Product snack= new Product(102, "Potato Chips", 2.00, ProductType.SNACK);
        Product bev  = new Product(103, "Soda Can",      2.50, ProductType.BEVERAGE);
        vm.loadProduct(1, choc, 5);
        vm.loadProduct(2, snack, 3);
        vm.loadProduct(3, bev,   2);
        vm.showInventory();

        // Scenario 1: Exact payment
        System.out.println("========== Scenario 1: Exact Payment ==========");
        vm.insertMoney(1.50);
        vm.selectProduct(1);

        // Scenario 2: Overpayment & Change
        System.out.println("========== Scenario 2: Overpayment & Change ==========");
        vm.insertMoney(3.00);
        vm.selectProduct(2);

        // Scenario 3: Underpayment & Refund
        System.out.println("========== Scenario 3: Underpayment & Refund ==========");
        vm.insertMoney(1.00);
        vm.selectProduct(3);

        // Scenario 4: Deplete Rack & Retry
        System.out.println("========== Scenario 4: Deplete Rack 3 & Retry ==========");
        vm.insertMoney(5.00); vm.selectProduct(3);
        vm.insertMoney(2.50); vm.selectProduct(3);
        vm.insertMoney(2.50); vm.selectProduct(3);

        vm.showInventory();
    }
}