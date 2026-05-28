package solid.a_single_responsibility.solution;

public class StudentValidator {

    // validation
    public void validate(Student s) {
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }
    }
}
