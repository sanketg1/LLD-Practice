package DesignPatterns.Creational.Factory;

public class Main {
    public static void main(String[] args) {
        Vehicle vehicle1 = VehicleFactory.getVehicle("Car");
        vehicle1.start();
        vehicle1.stop();
        Vehicle vehicle2 = VehicleFactory.getVehicle("Bike");
        vehicle2.start();
        vehicle2.stop();
        Vehicle vehicle3 = VehicleFactory.getVehicle("Truck");
        vehicle3.start();
        vehicle3.stop();
    }
}
