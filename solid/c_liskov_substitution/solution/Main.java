package solid.c_liskov_substitution.solution;

public class Main {
    public static void main(String[] args) {
        Rectangle r = new Rectangle(0, 0);
        r.setWidth(10);
        r.setHeight(5);
        System.out.println(r.calcArea()); // Expected:50, Actual:50

        Square s = new Square(5);
        s.setSide(10);
        System.out.println(s.calcArea()); // Expected:100, Actual:100
    }
}