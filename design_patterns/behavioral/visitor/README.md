# Visitor Pattern

Visitor е **Behavioral** pattern, с който добавяш **нови операции** върху йерархия от класове, без да модифицираш самите класове. Всяка операция се изнася в отделен "посетителски" клас, който има по един метод за всеки конкретен тип в йерархията.

В този пример йерархията е организационна структура: `Programmer → ProjectLead → Manager → VicePresident`. Върху нея са дефинирани две операции - `PrintVisitor` (принтира информация за всеки служител) и `AppraisalVisitor` (изчислява финален рейтинг по различна формула за всяка роля).

---

## Кога се ползва?

Когато:
- имаш стабилна йерархия от типове (рядко се добавят нови) и искаш лесно да добавяш нови операции върху нея,
- искаш логиката за дадена операция да живее на едно място (в Visitor класа), вместо разпръсната из всички класове в йерархията.

---

## Участници

| Роля | Клас |
|---|---|
| Visitor (интерфейс) | `Visitor` |
| ConcreteVisitor | `PrintVisitor`, `AppraisalVisitor` |
| Element (интерфейс) | `Employee` |
| ConcreteElement | `Programmer`, `ProjectLead`, `Manager`, `VicePresident` |
| AbstractElement | `AbstractEmployee` |
| ObjectStructure | `Client.visitOrgStructure` |
| Supporting | `PerformanceRating` |

---

## Имплементация

### `Visitor` - Visitor интерфейс

```java
public interface Visitor {

    void visit(Programmer programmer);

    void visit(ProjectLead lead);

    void visit(Manager manager);

    void visit(VicePresident vp);
}
```

По един `visit()` overload за всеки конкретен тип в йерархията. Нова операция = нов клас, имплементиращ `Visitor` - `Employee` класовете не се пипат.

### `Employee` - Element интерфейс

```java
public interface Employee {

    int getPerformanceRating();

    void setPerformanceRating(int rating);

    Collection<Employee> getDirectReports();

    int getEmployeeId();

    void accept(Visitor visitor);
}
```

`accept(Visitor visitor)` е единственото, което Visitor pattern добавя към `Employee`. Всеки конкретен клас го имплементира с `visitor.visit(this)`.

### `Programmer` / `ProjectLead` / `Manager` / `VicePresident` - ConcreteElement

```java
public class Programmer extends AbstractEmployee {

    private final String skill;

    public Programmer(String name, String skill) {
        super(name);
        this.skill = skill;
    }

    public String getSkill() {
        return skill;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this); // "this" е Programmer → Java избира visit(Programmer)
    }
}
```

`accept` изглежда еднакво във всички класове, но `this` има различен статичен тип за всеки - затова Java избира правилния `visit()` overload. Без `accept` не би могло да се случи: `visitor.visit(emp)` при `emp` деклариран като `Employee` не компилира (няма `visit(Employee)` overload).

`Manager`, `ProjectLead` и `VicePresident` добавят `List<Employee> directReports`, попълван в конструктора:

```java
public class Manager extends AbstractEmployee {

    private final List<Employee> directReports = new ArrayList<>();

    public Manager(String name, Employee... employees) {
        super(name);
        Arrays.stream(employees).forEach(directReports::add);
    }

    @Override
    public Collection<Employee> getDirectReports() {
        return directReports;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
```

### `PrintVisitor` - ConcreteVisitor

```java
public class PrintVisitor implements Visitor {

    @Override
    public void visit(Programmer programmer) {
        String msg = programmer.getName()
                + " is a "
                + programmer.getSkill()
                + " programmer";
        System.out.println(msg);
    }

    @Override
    public void visit(ProjectLead lead) {
        String msg = lead.getName()
                + " is a Project Lead with "
                + lead.getDirectReports().size()
                + " programmers reporting";
        System.out.println(msg);
    }

    @Override
    public void visit(Manager manager) {
        String msg = manager.getName()
                + " is a Manager with "
                + manager.getDirectReports().size()
                + " leads reporting";
        System.out.println(msg);
    }

    @Override
    public void visit(VicePresident vp) {
        String msg = vp.getName()
                + " is a VicePresident with "
                + vp.getDirectReports().size()
                + " managers reporting";
        System.out.println(msg);
    }
}
```

### `AppraisalVisitor` - ConcreteVisitor

```java
public class AppraisalVisitor implements Visitor {

    private final Ratings ratings = new Ratings();

    public class Ratings extends HashMap<Integer, PerformanceRating> {
        public int getFinalRating(int empId) {
            return get(empId).getFinalRating();
        }
    }

    @Override
    public void visit(Programmer programmer) {
        // 100% личен рейтинг
        PerformanceRating r = new PerformanceRating(programmer.getEmployeeId(), programmer.getPerformanceRating());
        r.setFinalRating(programmer.getPerformanceRating());
        ratings.put(programmer.getEmployeeId(), r);
    }

    @Override
    public void visit(ProjectLead lead) {
        // 75% личен + 25% екипен
        int teamAverage = getTeamAverage(lead);
        int rating = Math.round(0.75f * lead.getPerformanceRating() + 0.25f * teamAverage);
        // ...
    }

    @Override
    public void visit(Manager manager) {
        // 50% личен + 50% екипен
    }

    @Override
    public void visit(VicePresident vp) {
        // 25% личен + 75% екипен
    }

    public Ratings getFinalRatings() {
        return ratings;
    }
}
```

Всяка роля получава различна тежест между личен и екипен рейтинг - колкото по-висока позицията, толкова по-голяма е зависимостта от екипа.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        Employee emps = buildOrganization();
        Visitor visitor = new PrintVisitor();
        visitOrgStructure(emps, visitor);
    }

    private static void visitOrgStructure(Employee emp, Visitor visitor) {
        emp.accept(visitor);
        emp.getDirectReports().stream().forEach(e -> visitOrgStructure(e, visitor));
    }
}
```

`visitOrgStructure` обхожда дървото рекурсивно - първо посещава текущия служител, после директните му подчинени (depth-first).

---

## Употреба

Организационната структура:
```
VicePresident: Richard
├── Manager: Chad
│   └── ProjectLead: Tina
│       ├── Programmer: Rachel (C++)
│       └── Programmer: Andy (Java)
└── Manager: Chad II
    └── ProjectLead: Joey
        ├── Programmer: Josh (C#)
        └── Programmer: Bill (C++)
```

`visitOrgStructure` тръгва от `Richard` и обхожда depth-first:

```
Richard is a VicePresident with 2 managers reporting
Chad is a Manager with 1 leads reporting
Tina is a Project Lead with 2 programmers reporting
Rachel is a C++ programmer
Andy is a Java programmer
Chad II is a Manager with 1 leads reporting
Joey is a Project Lead with 2 programmers reporting
Josh is a C# programmer
Bill is a C++ programmer
```

---

## Pitfalls

- **Добавяне на нов Employee тип е трудно** - ако добавиш `Intern`, трябва да добавиш `visit(Intern)` в `Visitor` интерфейса и да го имплементираш в **всеки** съществуващ ConcreteVisitor (`PrintVisitor`, `AppraisalVisitor` и т.н.).
- **`accept` трябва да е във всеки клас поотделно** - не може да се наследи от `AbstractEmployee`, защото `this` трябва да е конкретният тип. Ако `AbstractEmployee.accept` извика `visitor.visit(this)`, `this` би бил `AbstractEmployee`, не `Programmer`.
- **`AppraisalVisitor` изисква `setPerformanceRating` да е извикан преди `visitOrgStructure`** - ако рейтингите не са зададени, `getTeamAverage` ще върне `0` и финалните рейтинги ще са безсмислени.
