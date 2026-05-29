package solid.d_interface_segregation.problematic_snippet;

public class Dog implements Animal {

    @Override
    public void eat() {
        System.out.println("Dog eats");
    }

    @Override
    public void drink() {
        System.out.println("Dog drinks");
    }

    @Override
    public void sleep() {
        System.out.println("Dog sleeps");
    }

    @Override
    public void swim() {
        System.out.println("Dog swims");
    }

    @Override
    public void fly() {
        throw new UnsupportedOperationException("Dog cannot fly.");
    }
}
