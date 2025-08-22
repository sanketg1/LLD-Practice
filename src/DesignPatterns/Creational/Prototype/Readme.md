🚀 Prototype Design Pattern: Clone It, Don’t Build It!
🔍 What is it?

The Prototype Design Pattern allows you to create new objects by copying (cloning) an existing object—the prototype—and changing only what’s necessary.

It’s like using a cookie cutter 🍪: one template, many cookies!

💡 Why Use It?

Imagine a game where you’re creating many similar characters. Do you really want to keep rewriting nearly identical code each time?

Without Prototype:

new Character("Knight", 100, 50, 1);
new Character("Mage", 100, 50, 1);  // Just changing the name!


With Prototype:

Character prototype = new Character("Default", 100, 50, 1);
Character mage = prototype.clone();
mage.setName("Mage");

⚠️ Problems with the Traditional Approach

❌ Code duplication

❌ Difficult to maintain (every new variation needs a new method)

❌ Poor scalability

✅ Prototype Pattern to the Rescue
Step-by-Step Java Implementation
public class Character implements Cloneable {
private String name;
private int health;
private int attackPower;
private int level;

public Character(String name, int health, int attackPower, int level) {
this.name = name;
this.health = health;
this.attackPower = attackPower;
this.level = level;
}

@Override
public Character clone() throws CloneNotSupportedException {
return (Character) super.clone();  // Shallow copy
}

// Setters for customization
public void setName(String name) { this.name = name; }
public void setLevel(int level) { this.level = level; }
public void setAttackPower(int power) { this.attackPower = power; }

public void showCharacterInfo() {
System.out.println("Character [Name=" + name + ", Health=" + health +
", AttackPower=" + attackPower + ", Level=" + level + "]");
}
}

Factory Using Prototype
public class CharacterFactory {
private Character prototypeCharacter;

public CharacterFactory() {
prototypeCharacter = new Character("Default", 100, 50, 1);
}

public Character createCharacter(String name, Integer level, Integer attackPower) throws CloneNotSupportedException {
Character clone = prototypeCharacter.clone();
if (name != null) clone.setName(name);
if (level != null) clone.setLevel(level);
if (attackPower != null) clone.setAttackPower(attackPower);
return clone;
}
}

Usage
CharacterFactory factory = new CharacterFactory();

Character mage = factory.createCharacter("Mage", null, null);
Character knight = factory.createCharacter("Knight", 5, null);
Character warrior = factory.createCharacter("Warrior", null, 80);

mage.showCharacterInfo();
knight.showCharacterInfo();
warrior.showCharacterInfo();

🎯 Benefits of the Prototype Pattern
Benefit	Description
✅ Less Code Duplication	Reuse existing object structure
✅ Easier Maintenance	Change prototype, not all instances
✅ Scalability	One clone method replaces many constructors or factory methods
✅ Flexibility	Add new properties easily without changing core logic
🌍 Real-World Use Cases

🎮 Game Dev: Creating similar NPCs or characters

📑 Document Templates: Modify cloned templates

🖥️ GUI Components: Clone UI elements like buttons or text fields

⚙️ Config Objects: Base config clone with user-specific overrides

🧠 Interview Insights

Q: How would you create 100 similar characters efficiently?
A: Use the Prototype Pattern to clone a base character and customize attributes.

Q: How do you scale character creation without bloating the factory?
A: By using a flexible createCharacter(...) method with optional parameters, reducing need for many method variants.

🏁 Final Thoughts

The Prototype Pattern is all about:

Reusability

Efficiency

Clean Code

It’s perfect when:

You need many similar objects.

You want to avoid repeated object initialization logic.

You want to isolate the process of creating complex or pre-configured objects.