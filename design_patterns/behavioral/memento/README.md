# Memento Pattern

Memento е **Behavioral** pattern, който позволява да се запази (и по-късно възстанови) вътрешното състояние на обект, без да се нарушава капсулацията му - никой отвън не вижда **какво** има в снимката, само я пази и я подава обратно.

В този пример Memento е комбиниран с **Command** (виж диаграмата от преди): всяка команда, преди да промени `WorkflowDesigner`, си взема "снимка" (memento) на текущото му състояние. При `undo()` командата просто връща тази снимка обратно - без да знае *как* да обърне конкретната операция.

---

## Кога се ползва?

Когато:
- искаш `undo`/`rollback` на промени в обект, но не искаш всяка операция да носи логика "как точно да се отмени",
- искаш снимка на състоянието на обект, без да изнасяш неговите вътрешни полета навън (capsule encapsulation) - снимката е "черна кутия" за всеки друг освен оригиналния обект.

---

## Участници

| Роля | Клас |
|---|---|
| Originator + Receiver | `WorkflowDesigner` |
| Memento | `WorkflowDesigner.Memento` |
| Command (abstract) | `WorkflowCommand` |
| ConcreteCommand + Caretaker | `AbstractWorkflowCommand`, `CreateCommand`, `AddStepCommand`, `RemoveStepCommand` |
| Client | `Client` |
| Value object | `Workflow` |

---

## Имплементация

### `Workflow` - стойностният обект, който се пази/възстановява

```java
public class Workflow {

    private final LinkedList<String> steps;

    private final String name;

    public Workflow(String name) {
        this.name = name;
        this.steps = new LinkedList<>();
    }

    public Workflow(String name, String... steps) {
        this.name = name;
        this.steps = new LinkedList<>();
        if (steps != null && steps.length > 0) {
            Arrays.stream(steps).forEach(s -> this.steps.add(s));
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Workflow [name=");
        builder.append(name).append("]\nBEGIN -> ");
        for (String step : steps) {
            builder.append(step).append(" -> ");
        }
        builder.append("END");
        return builder.toString();
    }

    public void addStep(String step) {
        steps.addLast(step);
    }

    public boolean removeStep(String step) {
        return steps.remove(step);
    }

    public String[] getSteps() {
        return steps.toArray(new String[steps.size()]);
    }

    public String getName() {
        return name;
    }
}
```

`steps` и `name` са `final` - веднъж зададени в конструктора, не се преписват. Промяна на workflow-а (`addStep`/`removeStep`) мутира **списъка** `steps`, не самото поле. Вторият конструктор приема `String... steps` (varargs) - удобен за `new Workflow(name, memento.getSteps())` при възстановяване.

### `WorkflowDesigner` - Originator + Receiver

```java
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
```

- **`getMemento()`** - прави снимка на текущото `workflow`. Ако още няма workflow (`workflow == null`), снимката е "празна" (`new Memento()` - и `steps`, и `name` остават `null`). Иначе снимката взема **копие** на стъпките (`workflow.getSteps()` връща нов `String[]` всеки път чрез `toArray`) и името.
- **`setMemento(memento)`** - обратното: ако снимката е "празна" (`isEmpty()` == `true`), значи преди тази команда не е имало workflow → връща `workflow = null`. Иначе пресъздава `Workflow` от сниматите `steps`/`name`.
- **`Memento`** е `static` nested class - не реферира конкретна инстанция на `WorkflowDesigner`, само пренася данни. Конструкторите и getter-ите са `private` - **само** `WorkflowDesigner` може да създава и да чете мементо. За всеки друг (вкл. командите) `Memento` е напълно "черна кутия" - точно затова `memento` полето в `AbstractWorkflowCommand` е от тип `WorkflowDesigner.Memento`, но никой извън `WorkflowDesigner` не вика `getSteps()`/`getName()`/`isEmpty()`.

### `WorkflowCommand` - Command interface

```java
public interface WorkflowCommand {

    void execute();

    void undo();
}
```

### `AbstractWorkflowCommand` - общата ConcreteCommand+Caretaker логика

```java
public abstract class AbstractWorkflowCommand implements WorkflowCommand {

    protected WorkflowDesigner.Memento memento;

    protected final WorkflowDesigner receiver;

    public AbstractWorkflowCommand(WorkflowDesigner designer) {
        this.receiver = designer;
    }

    @Override
    public void undo() {
        receiver.setMemento(memento);
    }
}
```

- **`receiver`** - `final`, зададен веднъж в конструктора - референция към **същата** `WorkflowDesigner` инстанция, която `Client` притежава.
- **`memento`** - **не** е `final`, защото се записва при всяко `execute()` (всяка подкласа го презаписва със своя снимка).
- **`undo()`** е общ за всички команди - просто връща `receiver` в записаното `memento` състояние. Подкласовете не пишат собствен `undo()`.

### `CreateCommand` / `AddStepCommand` / `RemoveStepCommand` - ConcreteCommand

```java
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
```

```java
public class AddStepCommand extends AbstractWorkflowCommand {

    private final String step;

    public AddStepCommand(WorkflowDesigner designer, String step) {
        super(designer);
        this.step = step;
    }

    @Override
    public void execute() {
        this.memento = receiver.getMemento();
        receiver.addStep(step);
    }
}
```

```java
public class RemoveStepCommand extends AbstractWorkflowCommand {

    private final String step;

    public RemoveStepCommand(WorkflowDesigner designer, String step) {
        super(designer);
        this.step = step;
    }

    @Override
    public void execute() {
        memento = receiver.getMemento();
        receiver.removeStep(step);
    }
}
```

Всяка команда следва един и същ шаблон в `execute()`:
1. **снимка ПРЕДИ** промяната → `memento = receiver.getMemento()`,
2. **самата промяна** → `receiver.createWorkflow(...)` / `receiver.addStep(...)` / `receiver.removeStep(...)`.

`undo()` (наследен от `AbstractWorkflowCommand`) после връща `receiver` в снимката от стъпка 1.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        WorkflowDesigner designer = new WorkflowDesigner();
        LinkedList<WorkflowCommand> commands = runCommands(designer);
        designer.print();
        undoLastCommand(commands);
        designer.print();
        undoLastCommand(commands);
        designer.print();
        undoLastCommand(commands);
        designer.print();
        undoLastCommand(commands);
        designer.print();
        undoLastCommand(commands);
        designer.print();
    }

    private static void undoLastCommand(LinkedList<WorkflowCommand> commands) {
        if (!commands.isEmpty()) {
            commands.removeLast().undo();
        }
    }

    private static LinkedList<WorkflowCommand> runCommands(WorkflowDesigner designer) {
        LinkedList<WorkflowCommand> commands = new LinkedList<>();

        WorkflowCommand cmd = new CreateCommand(designer, "Leave Workflow");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer, "Create Leave Application");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer, "Submit Application");
        commands.addLast(cmd);
        cmd.execute();

        cmd = new AddStepCommand(designer, "Application Approved");
        commands.addLast(cmd);
        cmd.execute();

        return commands;
    }
}
```

- **`runCommands`** изпълнява 4 команди (`Create` + 3× `AddStep`) и ги пази в `commands`, в реда на изпълнение (`addLast`).
- **`undoLastCommand`** маха последно изпълнената команда (`removeLast`) и я отменя (`undo()`) - LIFO ред, като Ctrl+Z.
- `main` извежда `designer` 6 пъти, отменяйки по една команда между всеки две извеждания.

---

## Употреба

Изход:
```
Workflow [name=Leave Workflow]
BEGIN -> Create Leave Application -> Submit Application -> Application Approved -> END
Workflow [name=Leave Workflow]
BEGIN -> Create Leave Application -> Submit Application -> END
Workflow [name=Leave Workflow]
BEGIN -> Create Leave Application -> END
Workflow [name=Leave Workflow]
BEGIN -> END
null
null
```

- Първите 4 реда (по двойки) - workflow-ът намалява стъпка по стъпка с всеки `undo()`.
- 5-ти ред - `undo()` на `CreateCommand` връща `memento.isEmpty() == true` → `workflow = null` → `print()` извежда `"null"`.
- 6-ти ред - `commands` вече е празен, `undoLastCommand` не прави нищо, `designer.print()` пак извежда `"null"`.

---

## Pitfalls

- **`getSteps()` връща копие, не референция** - `steps.toArray(...)` всеки път създава **нов** масив. Ако вместо това връщаше директно `steps` (самия `LinkedList`), мементото нямаше да е истинска "снимка" - по-късни промени в живия `workflow.steps` биха се "процедили" обратно в запазената снимка и `undo()` би върнал грешно (мутирано) състояние.
- **Без redo** - `undoLastCommand` прави `commands.removeLast()`, т.е. отменената команда **изчезва** от списъка завинаги. Няма начин да се "redo"-не след `undo()`.
- **`RemoveStepCommand` е дефинирана, но никъде не се ползва** в `Client` - демонстрацията изпълнява само `CreateCommand`/`AddStepCommand`.
- **`println(workflow)` при `workflow == null`** извежда буквалния стринг `"null"` - няма по-приятелско съобщение тип "No workflow yet".
