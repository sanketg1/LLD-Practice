package DesignPatterns.Creational.Factory;

public class Bike implements Vehicle {
    @Override
    public void start() {
        System.out.println("bike started");
    }
    @Override
    public void stop() {
        System.out.println("bike stopped");
    }
}
