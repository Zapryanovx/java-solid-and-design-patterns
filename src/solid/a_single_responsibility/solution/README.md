# SRP — Рефакторинг

Проблемният `StudentService` нарушаваше SRP, защото имаше **4 причини да се промени**:

```java
public class StudentService {
    private final HashMap<String, Student> students;

    public void addStudent(Student s) {
        // validation
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }

        // some business logic
        students.put(s.getName(), s);

        // save to file
        try (FileWriter fw = new FileWriter("students.txt", true)) {
            fw.write(s.getName() + "," + s.getFn() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // notify
        System.out.println("Email sent to: " + s.getFn());
    }
}
```

---

## Решение - 1 клас = 1 отговорност

### `StudentValidator` - само валидация

```java
public class StudentValidator {
    public void validate(Student s) {
        if (s == null || s.getName() == null || s.getName().isBlank()) {
            throw new IllegalArgumentException("Invalid student.");
        }
    }
}
```

### `StudentRepository` - само in-memory хранилище

```java
public class StudentRepository {
    private final HashMap<String, Student> students;

    public StudentRepository() {
        students = new HashMap<>();
    }

    public void save(Student s) {
        students.put(s.getName(), s);
    }
}
```

### `StudentFileWriter` - само запис във файл

```java
public class StudentFileWriter {
    public void write(Student s) {
        try (FileWriter fw = new FileWriter("students.txt", true)) {
            fw.write(s.getName() + "," + s.getFn() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### `NotificationService` - само нотификации

```java
public class NotificationService {
    public void notify(Student s) {
        System.out.println("Email sent to: " + s.getFn());
    }
}
```

### `StudentService` - само оркестрация (координира другите класове)

```java
public class StudentService {
    private final StudentValidator validator;
    private final StudentRepository repository;
    private final StudentFileWriter fileWriter;
    private final NotificationService notifier;

    public void addStudent(Student s) {
        validator.validate(s);
        repository.save(s);
        fileWriter.write(s);
        notifier.notify(s);
    }
}
```

---

## Резултат

| Клас | Причина за промяна |
|------|--------------------|
| `StudentValidator` | Промяна в правилата за валидация |
| `StudentRepository` | Промяна в in-memory структурата |
| `StudentFileWriter` | Промяна в начина на запис във файл |
| `NotificationService` | Промяна в начина на уведомяване |
| `StudentService` | Промяна в бизнес логиката |
