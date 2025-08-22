package DesignPatterns.Creational.AbstractFactory;

// Client Code
public class Main {
    public static void main(String[] args) {
        VehicleFactory hondaFactory = new HondaFactory();
        Vehicle honda = hondaFactory.createVehicle();
        honda.start();
        honda.stop();
        VehicleFactory toyotaFactory = new ToyotaFactory();
        Vehicle toyota = toyotaFactory.createVehicle();
        toyota.start();
        toyota.stop();
    }
}