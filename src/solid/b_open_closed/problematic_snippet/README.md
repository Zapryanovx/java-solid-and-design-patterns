# Open/Closed Principle (OCP)

> "Software entities should be open for extension, but closed for modification."
> — Robert C. Martin

## Какво е OCP?

Open/Closed Principle е вторият принцип от **SOLID**. Той гласи, че всеки клас трябва да е:
- **Open for extension** - можеш да добавяш ново поведение
  - **Closed for modification** - без да променяш съществуващия код

Идеята е: когато имаш ново изискване, **добавяш** нов клас вместо да **редактираш** стар. Така не рискуваш да счупиш вече работещ изтестван код.

---

## Нарушение на OCP

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

### Проблемен клас

```java
public class Employee {
    private static final double DEVELOPER_COEFF = 1.2;
    private static final double MANAGER_COEFF = 1.5;

    private final String name;
    private final Position position;

    public double calculateSalary(Employee e) {
        if (e.getPosition() == Position.DEVELOPER) {
            return e.getPosition().getBaseSalary() * DEVELOPER_COEFF;
        } else if (e.getPosition() == Position.MANAGER) {
            return e.getPosition().getBaseSalary() * MANAGER_COEFF;
        }
        // if you add a new position type you should update this method
        return 0.0;
    }
}
```

### Защо е проблем?

При всяка нова позиция трябва да **модифицираш** `Employee`:

```java
} else if (e.getPosition() == Position.INTERN) {
    return e.getPosition().getBaseSalary() * INTERN_COEFF;
}
```

`Employee` не е **затворен за модификация** — има толкова причини да се промени, колкото са позициите в системата.

| Нова позиция | Действие |
|--------------|----------|
| `INTERN` | Редактираш `Employee` |
| `TEAM_LEAD` | Редактираш `Employee` |
| `DIRECTOR` | Редактираш `Employee` |
