package solid.d_interface_segregation.solution;

public class Dog implements Eatable, Drinkable, Sleepable, Swimmable {

    @Override
    public void eat() {
        System.out.println("Eating Dog");
    }

    @Override
    public void drink() {
        System.out.println("Drinking Dog");
    }

    @Override
    public void sleep() {
        System.out.println("Sleeping Dog");
    }

    @Override
    public void swim() {
        System.out.println("Swimming Dog");
    }
}
