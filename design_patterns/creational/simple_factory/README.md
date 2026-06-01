# Simple Factory Pattern

Simple Factory е **Creational** pattern, който централизира създаването на обекти в един клас. Не е официален GoF pattern, но се използва широко в практиката.

---

## Кога се ползва?

Когато клиентът не трябва да знае **кой конкретен клас** да създаде:

```java
// Без Simple Factory - клиентът знае за всички типове
Post post = new BlogPost();
Post post = new NewsPost();

// С Simple Factory - клиентът знае само за Factory
Post post = PostFactory.createPost("blog");
```

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Product | `Post` |
| Concrete Products | `BlogPost`, `NewsPost`, `ProductPost` |
| Factory | `PostFactory` |
| Client | `Client` |

---

## Имплементация

### `Post` - абстрактен продукт

```java
public abstract class Post {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    // getters & setters...
}
```

### `NewsPost` - конкретен продукт

```java
public class NewsPost extends Post {
    private String headline;
    private LocalDate newsTime;
    // getters & setters...
}
```

### `PostFactory` - фабриката

```java
public class PostFactory {
    public static Post createPost(String type) {
        return switch (type) {
            case "blog"    -> new BlogPost();
            case "news"    -> new NewsPost();
            case "product" -> new ProductPost();
            default -> throw new IllegalArgumentException("Invalid post type");
        };
    }
}
```

### `Client`

```java
Post post = PostFactory.createPost("blog");
```

---

## Pitfalls

- Добавянето на нов тип изисква **модификация на `PostFactory`** - нарушава OCP
- Ако логиката за избор стане сложна - използвай **Factory Method** вместо това
