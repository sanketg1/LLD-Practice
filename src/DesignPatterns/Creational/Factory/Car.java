package DesignPatterns.Creational.Factory;

public class Car implements Vehicle {
    @Override
    public void start() {
        System.out.println("car started");
    }
    @Override
    public void stop() {
        System.out.println("car stopped");
    }
}
