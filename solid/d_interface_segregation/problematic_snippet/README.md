# Interface Segregation Principle (ISP)

> "Clients should not be forced to depend on interfaces they do not use."
> - Robert C. Martin

## Какво е ISP?

Interface Segregation Principle е четвъртият принцип от **SOLID**. Той гласи, че е по-добре да имаш **много малки специфични интерфейси** отколкото **един голям общ**. Класовете не трябва да бъдат принудени да имплементират методи, които не им трябват.

---

## Нарушение на ISP

### `Animal` - твърде голям интерфейс

```java
public interface Animal {
    void eat();
    void drink();
    void sleep();
    void swim();
    void fly();
}
```

### `Dog`

```java
public class Dog implements Animal {
    public void eat()   { System.out.println("Dog eats"); }
    public void drink() { System.out.println("Dog drinks"); }
    public void sleep() { System.out.println("Dog sleeps"); }
    public void swim()  { System.out.println("Dog swims"); }

    public void fly() {
        throw new UnsupportedOperationException("Dog cannot fly."); // ❌
    }
}
```

### `Eagle`

```java
public class Eagle implements Animal {
    public void eat()   { System.out.println("Eagle eats"); }
    public void drink() { System.out.println("Eagle drinks"); }
    public void sleep() { System.out.println("Eagle sleeps"); }
    public void fly()   { System.out.println("Eagle flies"); }

    public void swim() {
        throw new UnsupportedOperationException("An eagle cannot swim"); // ❌
    }
}
```

### Защо е проблем?

`Dog` и `Eagle` са принудени да имплементират методи, които не им принадлежат:

| Клас | Ненужен метод | Последствие |
|------|--------------|-------------|
| `Dog` | `fly()` | Хвърля `UnsupportedOperationException` |
| `Eagle` | `swim()` | Хвърля `UnsupportedOperationException` |

Ако добавиш нов метод в `Animal` (напр. `run()`), **всички класове** са принудени да го имплементират - дори тези, за които няма смисъл.
