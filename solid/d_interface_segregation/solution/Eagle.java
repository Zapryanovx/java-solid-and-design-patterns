package solid.d_interface_segregation.solution;

public class Eagle implements Eatable, Drinkable, Sleepable, Flyable {

    @Override
    public void eat() {
        System.out.println("Eating Eagle");
    }

    @Override
    public void drink() {
        System.out.println("Drinking Eagle");
    }

    @Override
    public void fly() {
        System.out.println("Flying Eagle");
    }

    @Override
    public void sleep() {
        System.out.println("Sleeping Eagle");
    }
}
