package solid.a_single_responsibility.problematic_snippet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class StudentService {
    private final HashMap<String, Student> students;

    public StudentService() {
        this.students = new HashMap<>();
    }

    public void addStudent(Student s) {

        // validation
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }

        // some business logic
        students.put(s.getName(), s);

        // save to file
        try (FileWriter fw = new FileWriter("students.txt", true)) {
            fw.write(s.getName() + "," + s.getFn() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // notify
        System.out.println("Email sent to: " + s.getFn());
    }
}