package DesignPatterns.Creational.AbstractFactory;

public class Toyota implements Vehicle{
    @Override
    public void start(){
        System.out.println("Toyota started");
    }
    @Override
    public void stop() {
        System.out.println("Toyota stopped");
    }
}
