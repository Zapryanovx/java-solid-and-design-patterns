# DIP - Рефакторинг

Проблемният `OrderService` нарушаваше DIP, защото създаваше сам инстанция на `MySQLDatabase` - директна зависимост от конкретна имплементация.

---

## Решение - зависимост от абстракция

### `Database` - абстракцията (мостът)

```java
public interface Database {
    void save(String data);
}
```

### `MySQLDatabase` - low-level модул зависи от абстракцията

```java
public class MySQLDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}
```

### `PostgreSQLDatabase` - low-level модул зависи от абстракцията

```java
public class PostgreSQLDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to PostgreSQL: " + data);
    }
}
```

### `OrderService` - high-level модул зависи от абстракцията

```java
public class OrderService {
    private final Database database;

    OrderService(Database database) { // зависимостта се инжектира отвън
        this.database = database;
    }

    public void placeOrder(String order) {
        database.save(order);
    }
}
```

---

## Нова база данни - само нов клас

```java
public class MongoDatabase implements Database {
    public void save(String data) {
        System.out.println("Saving to MongoDB: " + data);
    }
}
```

`OrderService` не се пипа — просто подаваш различна имплементация:

```java
OrderService service = new OrderService(new MySQLDatabase());
OrderService service = new OrderService(new PostgreSQLDatabase());
OrderService service = new OrderService(new MongoDatabase());
```

---

## Резултат

| | Проблемен код | След рефакторинг |
|--|---------------|-----------------|
| Зависимост | `OrderService` → `MySQLDatabase` | `OrderService` → `Database` ← `MySQLDatabase` |
| Смяна на БД | Редактираш `OrderService` | Само подаваш различна имплементация |
| DIP | Нарушен | Спазен |
