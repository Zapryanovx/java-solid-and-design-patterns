# Observer Pattern

Observer е **Behavioral** pattern за **one-to-many** зависимост между обекти: когато един обект ("subject") промени състоянието си, всички регистрирани при него обекти ("observers") автоматично биват известени - без subject-ът да знае нищо конкретно за тях, освен че имплементират общ интерфейс.

В този пример - `Order` е subject, който при добавяне на стока (`addItem`) известява регистрираните наблюдатели, а те преизчисляват части от поръчката (напр. `shippingCost`) според новото състояние.

---

## Кога се ползва?

Когато:
- промяна в един обект трябва автоматично да "разпространи" реакция в други обекти, без да въвеждаш polling ("проверявай периодично дали нещо се е променило"),
- subject-ът не трябва да знае **колко** observer-а има и **какво точно** правят - само че имплементират общ интерфейс.

---

## Участници

| Роля | Клас |
|---|---|
| ConcreteSubject | `Order` |
| Observer (interface) | `OrderObserver` |
| ConcreteObserver | `QuantityObserver`, `PriceObserver` |
| Client | `Client` |

---

## Имплементация

### `OrderObserver` - Observer interface

```java
//Abstract observer
public interface OrderObserver {

    void updated(Order order);
}
```

Единственият договор между `Order` и наблюдателите - `updated(order)`. Това е **"pull" модел**: подава се целия `Order`, а всеки observer сам "издърпва" каквото му трябва (`order.getCount()`, `order.getItemCost()`...), вместо subject-ът да решава какво точно да подаде.

### `Order` - ConcreteSubject

```java
public class Order {

    private final String id;

    private double shippingCost;

    //cost of items
    private double itemCost;

    private double discount;

    //no of items
    private int count;

    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(String id) {
        this.id = id;
    }

    public void attach(OrderObserver observer) {
        observers.add(observer);
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public double getTotal() {
        return itemCost - discount + shippingCost;
    }

    public void addItem(double price) {
        itemCost += price;
        count++;

        // may be in some method
        observers.forEach(o -> o.updated(this));
    }

    public int getCount() {
        return count;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setShippingCost(double cost) {
        this.shippingCost = cost;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Order#" + id + "\nItem cost:" + itemCost + "\nNo. of items:" + count
                + "\nShipping cost:" + shippingCost + "\nDiscount:" + discount
                + "\nTotal:" + getTotal();
    }
}
```

- **`observers`** - списъкът с регистрирани `OrderObserver`-и. `attach`/`detach` управляват регистрацията - всеки `OrderObserver` (без значение какъв конкретен клас е) може да бъде добавен/премахнат.
- **`addItem(price)`** - променя състоянието (`itemCost`, `count`), след което `observers.forEach(o -> o.updated(this))` известява **всички** регистрирани наблюдатели, подавайки `this` (целия `Order`).
- `Order` не знае нищо за `QuantityObserver`/`PriceObserver` конкретно - само за `OrderObserver` интерфейса.

### `QuantityObserver` - ConcreteObserver

```java
public class QuantityObserver implements OrderObserver {

    @Override
    public void updated(Order order) {
        int count = order.getCount();
        if (count <= 5) {
            order.setShippingCost(10);
        } else {
            order.setShippingCost(10 + (count - 5) * 1.5);
        }
    }
}
```

При всяко известие "издърпва" `order.getCount()` и преизчислява `shippingCost`: фиксирана такса `10` за до 5 артикула, + `1.5` за всеки следващ.

### `PriceObserver` (все още недовършен)

```java
//Concrete observer
public class PriceObserver {

}
```

Все още не имплементира `OrderObserver` и няма `updated(...)` - не може да бъде регистриран с `order.attach(...)`.

### `Client` (все още недовършен)

```java
public class Client {

    public static void main(String[] args) {

    }
}
```

---

## Употреба (илюстративно)

```java
Order order = new Order("1001");
order.attach(new QuantityObserver());

for (int i = 0; i < 6; i++) {
    order.addItem(10.0);
}

System.out.println(order);
```

Очакван изход:
```
Order#1001
Item cost:60.0
No. of items:6
Shipping cost:11.5
Discount:0.0
Total:71.5
```

При първите 5 извиквания на `addItem` -> `count` е 1..5 -> `QuantityObserver` държи `shippingCost = 10`. На 6-тото `addItem` -> `count == 6` -> `shippingCost = 10 + (6-5)*1.5 = 11.5`. `Order` никога не вика `setShippingCost` директно - всичко минава през `observers.forEach(o -> o.updated(this))`.

---

## Pitfalls

- **`import java.util.Observer;` е неизползван** - вгражданият `java.util.Observer`/`Observable` (deprecated от Java 9) няма нищо общо с нашия `OrderObserver`. Лесно се бърка от IDE auto-import заради еднаквото име `updated`/`update`.
- **Само `addItem` известява наблюдателите** - ако извикаш `setDiscount(...)` или `setShippingCost(...)` директно, `observers` **не** се известяват. Ако `PriceObserver` някога мени `discount`, това няма автоматично да преизчисли нещо друго (но и няма риск от безкрайна notify-рекурсия).
- **Реда на известяване = реда на `attach`** - ако два observer-а биха си противоречали (напр. и двата презаписват `shippingCost`), резултатът зависи от реда на регистрация.
- **`detach` съществува, но никъде не се демонстрира** - в момента няма пример как observer спира да получава известия.
