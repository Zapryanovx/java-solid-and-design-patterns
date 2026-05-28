package solid.a_single_responsibility.solution;

public class NotificationService {

    // notify
    public void notify(Student s) {
        System.out.println("Email sent to: " + s.getFn());
    }
}
