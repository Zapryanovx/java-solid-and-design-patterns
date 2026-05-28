# OCP - Рефакторинг

Проблемният `Employee` нарушаваше OCP, защото `calculateSalary` съдържаше if/else верига - всяка нова позиция изискваше модификация на съществуващ клас.

---

## Решение - абстрактен клас + наследяване

### `Position` (enum)

```java
public enum Position {
    DEVELOPER(2000),
    MANAGER(5000);

    private final int baseSalary;

    Position(int baseSalary) {
        this.baseSalary = baseSalary;
    }

    public int getBaseSalary() {
        return baseSalary;
    }
}
```

### `Employee` - абстрактен базов клас

```java
public abstract class Employee {
    private final String name;
    private final Position position;

    public Employee(String name, Position position) {
        this.name = name;
        this.position = position;
    }

    public abstract double calculateSalary();

    public String getName() { return name; }
    public Position getPosition() { return position; }
}
```

### `Developer`

```java
public class Developer extends Employee {
    private static final double DEVELOPER_COEFF = 1.2;

    public Developer(String name) {
        super(name, Position.DEVELOPER);
    }

    @Override
    public double calculateSalary() {
        return getPosition().getBaseSalary() * DEVELOPER_COEFF;
    }
}
```

### `Manager`

```java
public class Manager extends Employee {
    private static final double MANAGER_COEFF = 1.5;

    public Manager(String name) {
        super(name, Position.MANAGER);
    }

    @Override
    public double calculateSalary() {
        return getPosition().getBaseSalary() * MANAGER_COEFF;
    }
}
```

---

## Нова позиция - само нов клас

За да добавиш `Intern` не пипаш нищо съществуващо - само създаваш нов клас:

```java
public class Intern extends Employee {
    private static final double INTERN_COEFF = 0.5;

    public Intern(String name) {
        super(name, Position.INTERN);
    }

    @Override
    public double calculateSalary() {
        return getPosition().getBaseSalary() * INTERN_COEFF;
    }
}
```

---

## Резултат

| | Проблемен код | След рефакторинг |
|--|---------------|-----------------|
| Нова позиция | Редактираш `Employee` | Добавяш нов клас |
| Риск | Чупиш съществуващ код | Нулев риск за старото |
| OCP | Нарушен | Спазен |
