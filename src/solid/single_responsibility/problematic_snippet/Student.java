package solid.single_responsibility.problematic_snippet;

public class Student {
    private final String name;
    private String fn;

    public Student(String name, String fn) {
        this.name = name;
        this.fn = fn;
    }

    public String getName() {
        return name;
    }

    public String getFn() {
        return fn;
    }
}
