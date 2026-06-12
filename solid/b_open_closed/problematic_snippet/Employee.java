package solid.b_open_closed.problematic_snippet;

public class Employee {
    private static final double DEVELOPER_COEFF = 1.2;
    private static final double MANAGER_COEFF = 1.5;

    private final String name;
    private final Position position;
    // ...

    public Employee(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public double calculateSalary(Employee e) {
        if (e.getPosition() == Position.DEVELOPER) {
            return e.getPosition().getBaseSalary() * DEVELOPER_COEFF;
        } else if (e.getPosition() == Position.MANAGER) {
            return e.getPosition().getBaseSalary() * MANAGER_COEFF;
        }
        // if you add a new position type you should update this method

        return 0.0;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }
}
