# Factory Method Pattern

Factory Method е **Creational** pattern, който делегира създаването на обекти на наследниците. За разлика от Simple Factory, спазва **OCP** - нов тип = нов клас.

---

## Кога се ползва?

Когато искаш да разширяваш създаването на обекти **без да модифицираш съществуващ код**.

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Creator | `MessageCreator` |
| Concrete Creators | `JSONMessageCreator`, `TextMessageCreator` |
| Abstract Product | `Message` |
| Concrete Products | `JSONMessage`, `TextMessage` |
| Client | `Client` |

---

## Имплементация

### `Message` - абстрактен продукт

```java
public abstract class Message {
    public abstract String getContent();
    public void addDefaultHeaders() { }
    public void encrypt() { }
}
```

### `MessageCreator` - абстрактен creator

```java
public abstract class MessageCreator {

    public Message getMessage() {
        Message msg = createMessage();
        msg.addDefaultHeaders();
        msg.encrypt();
        return msg;
    }

    // Factory Method - наследниците го имплементират
    public abstract Message createMessage();
}
```

### `JSONMessageCreator` - конкретен creator

```java
public class JSONMessageCreator extends MessageCreator {
    @Override
    public Message createMessage() {
        return new JSONMessage();
    }
}
```

### `Client`

```java
printMessage(new JSONMessageCreator());
printMessage(new TextMessageCreator());

public static void printMessage(MessageCreator creator) {
    Message msg = creator.getMessage();
    System.out.println(msg);
}
```

---

## Разлика от Simple Factory

| | Simple Factory | Factory Method |
|--|---------------|----------------|
| Нов тип | Редактираш фабриката | Добавяш нов клас |
| OCP | Нарушен | Спазен |
| Сложност | По-проста | По-сложна |

---

## Pitfalls

- Повече класове - всеки тип изисква отделен Creator клас
- Трудно се рефакторира от Simple Factory
- Понякога създаваш цял клас само за да върнеш един обект
