package design_patterns.behavioral.memento.command;

public interface WorkflowCommand {

    void execute();

    void undo();
}
