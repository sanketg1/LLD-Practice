package DesignPatterns.Creational.AbstractFactory;

public class ToyotaFactory implements VehicleFactory {
    public Vehicle createVehicle() {
        return new Toyota();
    }
}
