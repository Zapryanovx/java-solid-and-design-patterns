package solid.single_responsibility.solution;

import java.util.HashMap;

public class StudentRepository {
    private final HashMap<String, Student> students;

    public StudentRepository() {
        students = new HashMap<>();
    }

    public void save(Student s) {
        students.put(s.getName(), s);
    }

    // ...
}
