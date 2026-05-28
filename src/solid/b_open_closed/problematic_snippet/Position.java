package solid.b_open_closed.problematic_snippet;

public enum Position {
    DEVELOPER(2000),
    MANAGER(5000);

    private final int baseSalary;
    Position(int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public int getBaseSalary() {
        return baseSalary;
    }
}
