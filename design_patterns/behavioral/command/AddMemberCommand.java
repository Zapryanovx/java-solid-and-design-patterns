package design_patterns.behavioral.command;

//A Concrete implementation of Command.
public class AddMemberCommand implements Command {

    private final String emailAddress;
    private final String listName;
    private final EWSService receiver;

    public AddMemberCommand(
            String emailAddress, String listName, EWSService receiver) {
        this.emailAddress = emailAddress;
        this.listName = listName;
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.addMember(emailAddress, listName);
    }
}
