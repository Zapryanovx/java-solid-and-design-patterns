# Chain of Responsibility Pattern

Chain of Responsibility е **Behavioral** pattern, при който заявка минава през **верига от handler-и** - всеки проверява "мога ли аз да я обработя?" и ако не може, я подава на следващия. Sender-ът знае само за **първия** handler във веригата - не знае колко handler-и има, нито кой точно ще обработи заявката.

---

## Кога се ползва?

Когато една заявка може да бъде обработена от **няколко различни обекта**, но кой точно зависи от условия, които **sender-ът не би трябвало да познава**.

В този пример - заявка за отпуска (`LeaveApplication`) минава през йерархия от одобряващи (`Manager` → `ProjectLead` → ...), всеки от които одобрява само определени видове/продължителности отпуски. Ако никой не одобри - заявката остава `Pending`.

---

## Участници

| Роля | Клас |
|---|---|
| Handler (interface) | `LeaveApprover` |
| Abstract Handler | `Employee` |
| Concrete Handler | `Manager`, `ProjectLead` (`Director` - все още stub) |
| Request | `LeaveApplication` |
| Client | `Client` |

---

## Имплементация

### `LeaveApplication` - Request

```java
public class LeaveApplication {
    public enum Type {Sick, PTO, LOP}
    public enum Status {Pending, Approved, Rejected}

    public void approve(String approverName) {
        this.status = Status.Approved;
        this.processedBy = approverName;
    }

    public static Builder getBuilder() {
        return new Builder();
    }
    // ...
}
```

Заявката се конструира чрез вградения `Builder` (`LeaveApplication.getBuilder().withType(...).from(...).to(...).build()`).

### `LeaveApprover` - Handler interface

```java
public interface LeaveApprover {
    void processLeaveApplication(LeaveApplication application);
    String getApproverRole();
}
```

### `Employee` - Abstract Handler (Template Method)

```java
public abstract class Employee implements LeaveApprover {

    private String role;
    private LeaveApprover successor;

    public Employee(String role, LeaveApprover successor) {
        this.role = role;
        this.successor = successor;
    }

    @Override
    public void processLeaveApplication(LeaveApplication application) {
        if (!processRequest(application) && successor != null) {
            successor.processLeaveApplication(application);
        }
    }

    protected abstract boolean processRequest(LeaveApplication application);

    @Override
    public String getApproverRole() {
        return role;
    }
}
```

`processLeaveApplication` е **template method** - общата логика на веригата ("опитай; ако не успееш и има следващ - подай нататък") живее тук **веднъж**. Всеки конкретен handler implement-ва само `processRequest` - своето собствено правило, и връща `true`/`false` дали е обработил заявката.

### `Manager` - Concrete Handler

```java
public class Manager extends Employee {

    public Manager(LeaveApprover successor) {
        super("Manager", successor);
    }

    @Override
    protected boolean processRequest(LeaveApplication application) {
        switch (application.getType()) {
        case Sick:
            application.approve(getApproverRole());
            return true;
        case PTO:
            if (application.getNoOfDays() <= 5) {
                application.approve(getApproverRole());
                return true;
            }
        }
        return false;
    }
}
```

`Manager` одобрява всяка `Sick` отпуска и `PTO` до 5 дни. За всичко друго (включително `LOP`, или `PTO` над 5 дни) връща `false` → заявката отива към `successor`.

### `ProjectLead` - Concrete Handler

```java
public class ProjectLead extends Employee {

    public ProjectLead(LeaveApprover successor) {
        super("Project Lead", successor);
    }

    @Override
    protected boolean processRequest(LeaveApplication application) {
        // type is sick leave & duration is less than or equal to 2 days
        if (application.getType() == LeaveApplication.Type.Sick && application.getNoOfDays() <= 2) {
            application.approve(getApproverRole());
            return true;
        }
        return false;
    }
}
```

### `Director` - все още stub

`Director` е оставен коментиран - би бил следващ handler във веригата (например за одобряване на `PTO` без лимит на дните).

---

## Употреба (илюстративно - `Client.java` все още е празен)

```java
LeaveApprover projectLead = new ProjectLead(null);
LeaveApprover manager = new Manager(projectLead);

LeaveApplication application = LeaveApplication.getBuilder()
        .withType(LeaveApplication.Type.PTO)
        .from(LocalDate.of(2026, 6, 15))
        .to(LocalDate.of(2026, 6, 18))
        .build();

manager.processLeaveApplication(application);

System.out.println(application);
```

Очакван изход:
```
PTO leave for 3 day(s) Approved by Manager
```

`Client`-ът знае само за `manager` (главата на веригата) - нищо за `ProjectLead`, нито за правилата им.

---

## Pitfalls

- **Връщане на `true` без `approve()`** - всеки `processRequest`, който връща `true`, трябва да остави заявката в коректен краен статус (`approve`/`reject`). Ако само върне `true` без да я обработи, веригата спира, но `LeaveApplication` остава `Pending` - "мълчаливо изгубена" заявка.
- **`successor == null` в края на веригата** - ако никой handler не обработи заявката, тя просто остава `Pending` без грешка/notification - няма "default" handler.
- **Подредбата на веригата има значение** - тук `Manager` е пръв и поема всички `Sick` заявки (включително тези ≤ 2 дни, които `ProjectLead` също одобрява) - `ProjectLead`-овото правило за `Sick` никога не се достига при тази подредба.
- **`switch` без `default`** - в `Manager.processRequest` `case PTO` няма `break`, но няма и `case` след него, така че пада коректно на `return false`. При добавяне на нов `case` след `PTO` без `return`/`break` лесно се получава неволен fall-through.
