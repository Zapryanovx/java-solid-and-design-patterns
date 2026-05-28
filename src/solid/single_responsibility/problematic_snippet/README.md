# Single Responsibility Principle (SRP)

> "A class should have only one reason to change."
> — Robert C. Martin

## Какво е SRP?

Single Responsibility Principle е първият принцип от **SOLID**. Той гласи, че всеки клас трябва да има **само една отговорност** или **само една причина** да бъде променена.

Ако еднин клас се занимава например с валидация, запис в база данни и изпращане на нотификации едновременно, тя има много причини да се промени. Това прави кода труден за поддръжка, тестване и разширяване.


---

## Нарушение на SRP

### Entity

```java
public class Student {
    private final String name;
    private String fn;

    public Student(String name, String fn) {
        this.name = name;
        this.fn = fn;
    }

    public String getName() { return name; }
    public String getFn() { return fn; }
}
```

### Проблемен клас

```java
public class StudentService {
    private final HashMap<String, Student> students;

    public void addStudent(Student s) {

        // Отговорност 1: Валидация
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }

        // Отговорност 2: Бизнес логика
        students.put(s.getName(), s);

        // Отговорност 3: Запис във файл
        try (FileWriter fw = new FileWriter("students.txt", true)) {
            fw.write(s.getName() + "," + s.getFn() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Отговорност 4: Нотификация
        System.out.println("Email sent to: " + s.getFn());
    }
}
```

### Защо е проблем?

`StudentService` има **4 причини да се промени**:

| Причина за промяна | Засегната логика |
|--------------------|-----------------|
| Промяна в правилата за валидация | `if` блокът в `addStudent` |
| Смяна на хранилището (файл → БД) | `FileWriter` блокът |
| Смяна на начина за нотификация | `System.out.println` редът |
| Промяна в бизнес правилата | `students.put(...)` |

Например, ако смениш начина за нотификация, трябва да пипаш `StudentService`въпреки че той не е клас за нотификации.