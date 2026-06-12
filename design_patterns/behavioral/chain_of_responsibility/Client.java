package design_patterns.behavioral.chain_of_responsibility;

import java.time.LocalDate;

public class Client {

    public static void main(String[] args) {
        LeaveApplication application = LeaveApplication.getBuilder()
                .withType(LeaveApplication.Type.Sick)
                .from(LocalDate.now())
                .to(LocalDate.now().plusDays(10))
                .build();

        System.out.println(application);
        System.out.println("------------------------");

        LeaveApprover approver = createChain();
        approver.processLeaveApplication(application);
        System.out.println(application);
    }

    private static LeaveApprover createChain() {
        Director director = new Director(null);
        Manager manager = new Manager(director);
        return new ProjectLead(manager);
    }
}
