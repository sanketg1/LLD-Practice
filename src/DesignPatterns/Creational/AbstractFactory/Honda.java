package DesignPatterns.Creational.AbstractFactory;

public class Honda implements Vehicle{
    @Override
    public void start() {
        System.out.println("Honda started");
    }

    @Override
    public void stop() {
        System.out.println("Honda stopped");
    }
}
