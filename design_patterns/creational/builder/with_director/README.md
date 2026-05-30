# Builder - с отделен Director

Builder и продуктът са **отделни класове**. Director клас оркестрира стъпките на строене.

---

## Структура

| Роля | Клас |
|------|------|
| Abstract Product | `UserDTO` (interface) |
| Abstract Builder | `UserDTOBuilder` (interface) |
| Concrete Product | `UserWebDTO` |
| Concrete Builder | `UserWebDTOBuilder` |
| Director | `Client.directBuild()` |

---

## Как работи

```java
// 1. Избираш конкретен Builder
UserDTOBuilder builder = new UserWebDTOBuilder();

// 2. Director оркестрира стъпките
private static UserDTO directBuild(UserDTOBuilder builder, User user) {
    return builder.withFirstName(user.getFirstName())
                  .withLastName(user.getLastName())
                  .withAddress(user.getAddress())
                  .withBirthday(user.getBirthday())
                  .build();
}
```

---

## Предимства

- Лесно се добавя нов Builder (напр. `UserRESTDTOBuilder`) без да пипаш останалото
- Director и Builder са независими — можеш да смениш единия без другия
- Подходящо когато имаш **много различни представяния** на един продукт
