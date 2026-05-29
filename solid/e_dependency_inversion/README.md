# Dependency Inversion Principle (DIP)

> "High-level modules should not depend on low-level modules. Both should depend on abstractions."
> - Robert C. Martin

High-level модулите (бизнес логика) не трябва да зависят директно от low-level модулите (конкретни имплементации). **И двата трябва да зависят от абстракция** - интерфейс или абстрактен клас, който служи като мост между тях.

## Нарушение

`OrderService` създава сам инстанция на `MySQLDatabase` - директно вграждане на конкретна имплементация. Смяна на базата данни изисква **редакция на бизнес логиката**.

```java
private final MySQLDatabase database = new MySQLDatabase(); // ❌
```

## Решение

Въвежда се `Database` интерфейс. `OrderService` зависи от него, а конкретната имплементация се **инжектира отвън**:

```java
OrderService service = new OrderService(new MySQLDatabase());
OrderService service = new OrderService(new PostgreSQLDatabase());
```

| Клас/Интерфейс | Роля |
|----------------|------|
| `Database` | Абстракцията (мостът) |
| `MySQLDatabase` | Low-level имплементация |
| `PostgreSQLDatabase` | Low-level имплементация |
| `OrderService` | High-level бизнес логика |

## Структура

```
problematic_snippet/   - OrderService директно зависи от MySQLDatabase
solution/              - OrderService зависи от Database интерфейс
```
