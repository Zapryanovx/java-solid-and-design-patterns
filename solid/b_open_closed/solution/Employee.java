package solid.b_open_closed.solution;

public abstract class Employee {
    private final String name;
    private final Position position;

    public Employee(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public abstract double calculateSalary();

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
}
