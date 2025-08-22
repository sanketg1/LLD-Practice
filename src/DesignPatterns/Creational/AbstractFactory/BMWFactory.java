package DesignPatterns.Creational.AbstractFactory;

public class BMWFactory implements VehicleFactory {
    public Vehicle createVehicle() {
        return new BMW();
    }
}
