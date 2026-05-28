package solid.single_responsibility.solution;

import solid.single_responsibility.Student;

public class StudentValidator {

    // validation
    public void validate(Student s) {
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }
    }
}
