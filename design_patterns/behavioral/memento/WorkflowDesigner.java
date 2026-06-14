package design_patterns.behavioral.memento;

public class WorkflowDesigner {

    private Workflow workflow;

    public void createWorkflow(String name) {
        workflow = new Workflow(name);
    }

    public Workflow getWorkflow() {
        return this.workflow;
    }

    public Memento getMemento() {
        if (workflow == null) {
            return new Memento();
        }
        return new Memento(workflow.getSteps(), workflow.getName());
    }

    public void setMemento(Memento memento) {
        if (memento.isEmpty()) {
            workflow = null;
            return;
        }
        workflow = new Workflow(memento.getName(), memento.getSteps());
    }

    public void addStep(String step) {
        workflow.addStep(step);
    }

    public void removeStep(String step) {
        workflow.removeStep(step);
    }

    public void print() {
        System.out.println(workflow);
    }

    //Memento
    public static class Memento {

        private final String[] steps;
        private final String name;

        private Memento() {
            this.steps = null;
            this.name = null;
        }

        private Memento(String[] steps, String name) {
            this.steps = steps;
            this.name = name;
        }

        private String[] getSteps() {
            return steps;
        }

        private String getName() {
            return name;
        }

        private boolean isEmpty() {
            return steps == null && name == null;
        }
    }
}
