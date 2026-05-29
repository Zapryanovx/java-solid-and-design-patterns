# Dependency Inversion Principle (DIP)

> "High-level modules should not depend on low-level modules. Both should depend on abstractions."
> - Robert C. Martin

## Какво е DIP?

Dependency Inversion Principle е петият принцип от **SOLID**. Той гласи, че:
- **High-level модулите** (бизнес логика) не трябва да зависят директно от **low-level модулите** (конкретни имплементации)
- **И двата** трябва да зависят от **абстракция** (интерфейс)

**абстракция ще рече интерфейс или абстрактен клас, който да служи като мост между тях**

Идеята е: вместо да вграждаш конкретна имплементация в класа, **инжектираш** зависимостта отвън.

---

## Нарушение на DIP

### `MySQLDatabase` - low-level модул

```java
public class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}
```

### `OrderService` - high-level модул

```java
public class OrderService {
    private final MySQLDatabase database = new MySQLDatabase(); // директна зависимост

    public void placeOrder(String order) {
        database.save(order);
    }
}
```

### Защо е проблем?

`OrderService` създава сам инстанция на `MySQLDatabase` — вързан е директно за конкретна имплементация:

| Искаш да смениш с... | Действие |
|----------------------|----------|
| `PostgreSQLDatabase` | Редактираш `OrderService` |
| `MongoDatabase` | Редактираш `OrderService` |
| `InMemoryDatabase` (за тестове) | Редактираш `OrderService` |

Всяка смяна на хранилището изисква да пипаш high-level бизнес логиката - DIP е нарушен.
