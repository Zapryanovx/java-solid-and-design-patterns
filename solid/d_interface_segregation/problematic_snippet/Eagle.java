package solid.d_interface_segregation.problematic_snippet;

public class Eagle implements Animal {

    @Override
    public void eat() {
        System.out.println("Eagle eats");
    }

    @Override
    public void drink() {
        System.out.println("Eagle drinks");
    }

    @Override
    public void sleep() {
        System.out.println("Eagle sleeps");
    }

    @Override
    public void swim() {
        throw new UnsupportedOperationException("An eagle cannot swim");
    }

    @Override
    public void fly() {
        System.out.println("Eagle flies");
    }
}
