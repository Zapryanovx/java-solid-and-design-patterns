# LSP - Рефакторинг

Проблемният `Square extends Rectangle` нарушаваше LSP, защото `Square` override-ваше setters-ите и счупваше очакваното поведение на `Rectangle` при заместване.

---

## Решение - обща абстракция без наследяване

`Rectangle` и `Square` са **независими класове** - никой не наследява никого. И двата имплементират общия интерфейс `Shape`.

### `Shape` - общ интерфейс

```java
public interface Shape {
    double calcArea();
}
```

### `Rectangle`

```java
public class Rectangle implements Shape {
    private double height;
    private double width;

    public Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public double calcArea() {
        return height * width;
    }

    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
}
```

### `Square`

```java
public class Square implements Shape {
    private double side;

    public Square(double side) {
        this.side = side;
    }

    @Override
    public double calcArea() {
        return side * side;
    }

    public void setSide(double side) { this.side = side; }
}
```

### `Main` - демонстрация

```java
Rectangle r = new Rectangle(0, 0);
r.setWidth(10);
r.setHeight(5);
System.out.println(r.calcArea()); // Expected:50, Actual:50 ✅

Square s = new Square(5);
s.setSide(10);
System.out.println(s.calcArea()); // Expected:100, Actual:100 ✅
```

---

## Резултат

| | Проблемен код | След рефакторинг |
|--|---------------|-----------------|
| Връзка | `Square extends Rectangle` | `Square` и `Rectangle` са независими |
| Заместване | `Square` счупва поведението на `Rectangle` | Всеки се държи предвидимо |
| LSP | Нарушен | Спазен |
