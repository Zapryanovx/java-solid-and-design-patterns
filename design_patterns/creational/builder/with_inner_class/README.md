# Builder - с Inner Class

Builder е **вложен статичен клас** вътре в продукта. Всичко е в един файл.

---

## Структура

| Роля | Клас |
|------|------|
| Product | `UserDTO` |
| Builder | `UserDTO.UserDTOBuilder` (static inner class) |
| Director | `Client.directBuild()` |

---

## Как работи

```java
// 1. Вземаш Builder чрез статичен метод на продукта
UserDTO.UserDTOBuilder builder = UserDTO.getBuilder();

// 2. Director оркестрира стъпките
private static UserDTO directBuild(UserDTO.UserDTOBuilder builder, User user) {
    return builder.withFirstName(user.getFirstName())
                  .withLastName(user.getLastName())
                  .withBirthday(user.getBirthday())
                  .withAddress(user.getAddress())
                  .build();
}
```

---

## Ключова разлика — private setters

Setters-ите на `UserDTO` са `private` — само вътрешният Builder може да ги вика:

```java
public class UserDTO {
    private void setName(String name) { ... }    // недостъпно отвън
    private void setAddress(String address) { ... }

    public static class UserDTOBuilder {
        public UserDTO build() {
            UserDTO dto = new UserDTO();
            dto.setName(...);    // ✅ inner class вижда private методи
            dto.setAddress(...);
            return dto;
        }
    }
}
```

---

## Предимства

- Всичко е в **един файл** — по-компактно
- Setters са `private` — обектът е **по-защитен**
- Подходящо когато имаш **едно представяне** на продукта
