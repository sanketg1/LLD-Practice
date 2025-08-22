package DesignPatterns.Creational.Factory;

public class Truck implements Vehicle {
    @Override
    public void start() {
        System.out.println("truck started");
    }
    @Override
    public void stop() {
        System.out.println("truck stopped");
    }
}
