package solid.b_open_closed.solution;

public class Manager extends Employee {
    private static final double MANAGER_COEFF = 1.5;

    public Manager(String name) {
        super(name, Position.MANAGER);
    }

    @Override
    public double calculateSalary() {
        return getPosition().getBaseSalary() * MANAGER_COEFF;
    }
}
