# Builder Pattern

Builder е **Creational** design pattern, който строи сложни обекти **стъпка по стъпка**. Позволява да създаваш различни представяния на един обект, използвайки един и същи процес на строене.

---

## Кога се ползва?

Когато обектът има **много полета** и не искаш конструктор с много параметри:

```java
// без Builder - нечетимо
new User("Ron", "Swanson", "1960-05-06", "100 State Street", "Pawnee", ...);

// с Builder - ясно и четимо
new UserWebDTOBuilder()
    .withFirstName("Ron")
    .withLastName("Swanson")
    .withBirthday(LocalDate.of(1960, 5, 6))
    .withAddress(address)
    .build();
```

---

## Участници

### `UserDTO` - абстрактен продукт

```java
public interface UserDTO {
    String getName();
    String getAddress();
    String getAge();
}
```

### `UserDTOBuilder` - абстрактен Builder

```java
public interface UserDTOBuilder {
    UserDTOBuilder withFirstName(String firstName);
    UserDTOBuilder withLastName(String lastName);
    UserDTOBuilder withBirthday(LocalDate date);
    UserDTOBuilder withAddress(Address address);
    UserDTO build();
    UserDTO getUserDTO();
}
```

### `UserWebDTO` - конкретен продукт

```java
public class UserWebDTO implements UserDTO {
    private String name;
    private String address;
    private String age;
    // ...
}
```

### `UserWebDTOBuilder` - конкретен Builder

```java
public class UserWebDTOBuilder implements UserDTOBuilder {

    @Override
    public UserDTOBuilder withBirthday(LocalDate date) {
        // изчислява възрастта от датата на раждане
        Period period = Period.between(date, LocalDate.now());
        this.age = Integer.toString(period.getYears());
        return this;
    }

    @Override
    public UserDTO build() {
        dto = new UserWebDTO(firstName + " " + lastName, address, age);
        return dto;
    }
}
```

### `Client` - Director

```java
public static void main(String[] args) {
    User user = createUser();
    UserDTOBuilder builder = new UserWebDTOBuilder();
    UserDTO dto = directBuild(builder, user);
    System.out.println(dto);
}

private static UserDTO directBuild(UserDTOBuilder builder, User user) {
    return builder.withFirstName(user.getFirstName())
                  .withLastName(user.getLastName())
                  .withAddress(user.getAddress())
                  .withBirthday(user.getBirthday())
                  .build();
}
```

---

## Изход

```
name=Ron Swanson
age=65
address=100, State Street
Pawnee
Indiana 47998
```

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Product | `UserDTO` |
| Abstract Builder | `UserDTOBuilder` |
| Concrete Product | `UserWebDTO` |
| Concrete Builder | `UserWebDTOBuilder` |
| Director | `Client` |
