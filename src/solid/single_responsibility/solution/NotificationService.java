package solid.single_responsibility.solution;

import solid.single_responsibility.Student;

public class NotificationService {

    // notify
    public void notify(Student s) {
        System.out.println("Email sent to: " + s.getFn());
    }
}
