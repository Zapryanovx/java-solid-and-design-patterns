package solid.single_responsibility.solution;

import solid.single_responsibility.Student;

import java.io.FileWriter;
import java.io.IOException;

public class StudentFileWriter {

    // save to file
    public void write(Student s) {
        try (FileWriter fw = new FileWriter("students.txt", true)) {
            fw.write(s.getName() + "," + s.getFn() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
