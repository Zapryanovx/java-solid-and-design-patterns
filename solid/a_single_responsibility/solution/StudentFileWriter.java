package solid.a_single_responsibility.solution;

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
