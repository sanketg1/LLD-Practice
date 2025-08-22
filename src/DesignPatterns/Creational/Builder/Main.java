package DesignPatterns.Creational.Builder;

public class Main {
    public static void main(String[] args) {
        Car.CarBuilder builder = new Car.CarBuilder();
        Car car1 = builder.setEngine("V8")
                .setColor("Red")
                .setSeats(5)
                .setSunroof(true)
                .build(); // The build method returns the final product
        System.out.println(car1);

        // Creating another car with different specifications
        Car car2 = builder.setEngine("V6")
                .setColor("Blue")
                .setSeats(4)
                .build(); // Sunroof and Navigation are default
        System.out.println(car2);
    }
}
