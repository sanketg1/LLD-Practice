🛠️ Builder Design Pattern
1. What is the Builder Design Pattern?

The Builder Design Pattern is a creational design pattern that helps in constructing complex objects step-by-step. It's particularly useful when:

An object has many attributes.

Some attributes are optional.

You want to avoid having large, confusing constructors.

Instead of using a constructor with many parameters (which can be hard to read and prone to mistakes), the Builder pattern allows for a flexible and readable way to construct objects.

In short:
✅ Separate object construction from the representation.
✅ Build objects piece by piece.
✅ Use .build() to get the final result.

2. The Traditional Way: Problems with Constructors ⚙️

Let's take an example using a Car class.

Java: Constructor-based Car
public class Car {
private String engine;
private int wheels;
private int seats;
private String color;
private boolean sunroof;
private boolean navigationSystem;

public Car(String engine, int wheels, int seats, String color,
boolean sunroof, boolean navigationSystem) {
this.engine = engine;
this.wheels = wheels;
this.seats = seats;
this.color = color;
this.sunroof = sunroof;
this.navigationSystem = navigationSystem;
}
}

🚫 Problem #1: Passing Unnecessary Values
Car car = new Car("V8", 4, 5, "Red", false, false);


You’re forced to pass values for all parameters — even if you don’t care about them.

⚒️ Problem #2: Constructor Overloading Explosion
public class Car {
public Car(String engine, int wheels, int seats, String color,
boolean sunroof, boolean navigationSystem) { ... }

public Car(String engine, int wheels, int seats, String color) { ... }

public Car(String engine, int wheels, int seats) { ... }
}


You’ll end up with many overloaded constructors just to cover different combinations.

📉 Problem #3: Lack of Readability
Car car = new Car("V8", 4, 5, "Red", true, false);


What do true and false mean? It’s not immediately clear.

3. Interviewer’s Follow-Up 🎤

❓ What happens if more optional parameters are added later?

You’ll need even more constructors.

The object creation becomes harder to maintain.

Readability and scalability suffer.

4. Shifting to the Builder Pattern 🔧
   🏗️ Why "Builder"?

Because the object is built step-by-step, with clear method calls for each property. You control the creation process in a modular and readable way.

5. Builder Pattern in Java: Step-by-Step
   public class Car {
   private String engine;
   private int wheels;
   private int seats;
   private String color;
   private boolean sunroof;
   private boolean navigationSystem;

// Private constructor used by Builder
private Car(CarBuilder builder) {
this.engine = builder.engine;
this.wheels = builder.wheels;
this.seats = builder.seats;
this.color = builder.color;
this.sunroof = builder.sunroof;
this.navigationSystem = builder.navigationSystem;
}

// Getters
public String getEngine() { return engine; }
public int getWheels() { return wheels; }
public int getSeats() { return seats; }
public String getColor() { return color; }
public boolean hasSunroof() { return sunroof; }
public boolean hasNavigationSystem() { return navigationSystem; }

@Override
public String toString() {
return "Car [engine=" + engine + ", wheels=" + wheels + ", seats=" + seats
+ ", color=" + color + ", sunroof=" + sunroof
+ ", navigationSystem=" + navigationSystem + "]";
}

// Nested Static Builder Class
public static class CarBuilder {
private String engine;
private int wheels = 4;
private int seats = 5;
private String color = "Black";
private boolean sunroof = false;
private boolean navigationSystem = false;

    public CarBuilder setEngine(String engine) {
      this.engine = engine;
      return this;
    }

    public CarBuilder setWheels(int wheels) {
      this.wheels = wheels;
      return this;
    }

    public CarBuilder setSeats(int seats) {
      this.seats = seats;
      return this;
    }

    public CarBuilder setColor(String color) {
      this.color = color;
      return this;
    }

    public CarBuilder setSunroof(boolean sunroof) {
      this.sunroof = sunroof;
      return this;
    }

    public CarBuilder setNavigationSystem(boolean navigationSystem) {
      this.navigationSystem = navigationSystem;
      return this;
    }

    public Car build() {
      return new Car(this);
    }
}
}

🧪 Client Code Example
public class Main {
public static void main(String[] args) {
Car.CarBuilder builder = new Car.CarBuilder();

    Car car1 = builder.setEngine("V8")
                      .setColor("Red")
                      .setSeats(5)
                      .setSunroof(true)
                      .build();

    System.out.println(car1);

    Car car2 = builder.setEngine("V6")
                      .setColor("Blue")
                      .setSeats(4)
                      .build();

    System.out.println(car2);
}
}

🤔 Why Nest the Builder Inside the Class?

Encapsulation: Keeps the CarBuilder close to the Car.

Access to Private Fields: Inner builder can set private fields directly.

Cleaner API: Logical grouping makes usage more intuitive.

❓ Why Is the Builder Static?

Doesn’t require an instance of Car to use the builder.

Prevents unnecessary object creation.

Simplifies usage: new Car.CarBuilder() is straightforward.

6. Solving Interview Follow-Ups with Builder 🛠️
   ✅ What if we only want to set some attributes?

Only call the setter methods you need — the rest get default values.

Car car = new Car.CarBuilder()
.setEngine("V8")
.setColor("Silver")
.build();

✅ What if we want to add more features?

Just add a new setter method in the CarBuilder class — no need to touch the constructor or client code.

7. Real-life Use Cases of the Builder Pattern 🏗️
   Scenario	Description
   🍔 Meal Orders	Build complex meal combos with optional toppings, sides, drinks, etc.
   📑 Document Creation	Add headers, footers, tables, images step-by-step in word processors.
   👤 User Profiles	Build profiles with required and optional fields like bio, profile pic, etc.
   🎉 Conclusion: Building Objects the Smart Way

The Builder Design Pattern is a powerful alternative to telescoping constructors. It allows you to:

Set only the properties you care about.

Avoid constructor overloading.

Provide default values.

Keep your code flexible, clean, and readable.

Whenever you're working with objects that have many parameters, especially with optional values, reach for the Builder Pattern for clean and maintainable code!