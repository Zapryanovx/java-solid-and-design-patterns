package design_patterns.behavioral.memento.command;

import design_patterns.behavioral.memento.WorkflowDesigner;

public class CreateCommand extends AbstractWorkflowCommand {

    private final String name;

    public CreateCommand(WorkflowDesigner designer, String name) {
        super(designer);
        this.name = name;
    }

    @Override
    public void execute() {
        this.memento = receiver.getMemento();
        receiver.createWorkflow(name);
    }
}
