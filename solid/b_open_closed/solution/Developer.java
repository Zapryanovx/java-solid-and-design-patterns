package solid.b_open_closed.solution;

import solid.b_open_closed.solution.Position;

public class Developer extends Employee {
    private static final double DEVELOPER_COEFF = 1.2;

    public Developer(String name) {
        super(name, Position.DEVELOPER);
    }

    @Override
    public double calculateSalary() {
        return getPosition().getBaseSalary() * DEVELOPER_COEFF;
    }
}
