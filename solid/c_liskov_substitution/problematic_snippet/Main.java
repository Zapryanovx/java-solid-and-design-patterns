package solid.c_liskov_substitution.problematic_snippet;

public class Main {
    public static void main(String[] args) {
        Rectangle r = new Rectangle(0, 0);
        r.setWidth(10);
        r.setHeight(5);
        System.out.println(r.calcArea());

        Rectangle r2 = new Square(0);
        r2.setWidth(10);
        r2.setHeight(5);
        System.out.println(r2.calcArea()); // Expected:50, Actual:25
    }
}