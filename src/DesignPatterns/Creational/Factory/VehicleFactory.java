package DesignPatterns.Creational.Factory;

public class VehicleFactory {
    public static Vehicle getVehicle(String vehicleType) {

        if (vehicleType == null) {
            return null;
        }
        if (vehicleType.equalsIgnoreCase("CAR")) {
            return new Car();
        } else if (vehicleType.equalsIgnoreCase("BIKE")) {
            return new Bike();
        }else if (vehicleType.equalsIgnoreCase("TRUCK")) {
            return new Truck();
        }
        return null;
    }
}
