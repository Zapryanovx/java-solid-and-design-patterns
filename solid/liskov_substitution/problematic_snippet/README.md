# Liskov Substitution Principle (LSP)

> "If S is a subtype of T, then objects of type T may be replaced with objects of type S without altering the correctness of the program."
> — Barbara Liskov

## Какво е LSP?

Liskov Substitution Principle е третият принцип от **SOLID**. Той гласи, че навсякъде където използваш даден клас, трябва да можеш да го замениш с негов наследник - без програмата да се счупи или да се държи неочаквано.

Накратко: **наследникът трябва да може да замести родителя си.**

---

## Нарушение на LSP

### `Rectangle`

```java
public class Rectangle {
    private double height;
    private double width;

    public Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
    }

    public double calcArea() {
        return width * height;
    }

    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
}
```

### `Square` - наследява `Rectangle`

```java
public class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }

    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        super.setWidth(height); // принудително равни страни
    }

    @Override
    public void setWidth(double width) {
        super.setHeight(width);
        super.setWidth(width); // принудително равни страни
    }
}
```

### `Main` - демонстрация на проблема

```java
Rectangle r = new Rectangle(0, 0);
r.setWidth(10);
r.setHeight(5);
System.out.println(r.calcArea()); // Еxpected:50

Rectangle r2 = new Square(0);
r2.setWidth(10);
r2.setHeight(5);
System.out.println(r2.calcArea()); // Expected:25, Actual:25
```

### Защо е проблем?

Заместихме `Rectangle` със `Square` — двата обекта получават **едни и същи извиквания**, но дават **различни резултати**:

| | `setWidth(10)` | `setHeight(5)` | `calcArea()` |
|--|----------------|----------------|--------------|
| `Rectangle` | width=10 | height=5 | **50** ✅ |
| `Square` | width=10, height=10 | height=5, width=5 | **25** ❌ |

`Square` override-ва setters-ите, за да поддържа инварианта си (равни страни), но с това **счупва поведението на `Rectangle`**. Програмата очаква 50, получава 25 - LSP е нарушен.
