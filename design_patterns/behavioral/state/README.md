# State Pattern

State е **Behavioral** pattern, при който поведението на обект зависи от **текущото му вътрешно състояние**. Вместо `if`/`switch` по статус навсякъде в кода, всяко състояние се изнася в отделен клас, имплементиращ общ интерфейс - контекстният обект просто делегира на "текущото си състояние" и (при нужда) преминава към друго.

В този пример `Order` минава през състояния `New -> Paid -> InTransit -> Delivered`, с възможност за `Cancelled` по пътя. Цената на анулирането (`cancel()`) зависи от това **в кое състояние** е поръчката в момента.

---

## Кога се ползва?

Когато:
- поведението на обект трябва да се различава според вътрешния му статус, и тези разлики растат (нов статус = нов `if`/`case` навсякъде),
- искаш добавянето на ново състояние да става чрез **нов клас**, не чрез пипане на съществуващи условия.

---

## Участници

| Роля | Клас |
|---|---|
| Context | `Order` |
| State (abstract) | `OrderState` |
| ConcreteState | `New`, `Paid`, `InTransit`, `Delivered`, `Cancelled` |
| Client | `Client` |

---

## Имплементация

### `OrderState` - State interface

```java
//Abstract state
public interface OrderState {

    double handleCancellation();
}
```

Единственото поведение, което зависи от състоянието, е "какво се случва (и каква сума се връща), ако поръчката бъде анулирана точно сега".

### `Order` - Context

```java
//Context class
public class Order {

    private OrderState currentState;

    public Order() {
        currentState = new New();
    }

    public double cancel() {
        double charges = currentState.handleCancellation();
        currentState = new Cancelled();
        return charges;
    }

    public void paymentSuccessful() {
        currentState = new Paid();
    }

    public void dispatched() {
        currentState = new InTransit();
    }

    public void delivered() {
        currentState = new Delivered();
    }
}
```

- **`currentState`** - стартира като `new New()` в конструктора.
- **`cancel()`** - истинската State-делегация: пита **текущото** състояние `handleCancellation()` (различен резултат за `New`/`Paid`/`InTransit`/`Delivered`/`Cancelled`), после преминава в `Cancelled`.
- **`paymentSuccessful()` / `dispatched()` / `delivered()`** - директни, безусловни преходи (`currentState = new X()`). Те **не** питат текущото състояние дали преходът е валиден - просто го презаписват.

### `New` / `Paid` / `InTransit` / `Delivered` / `Cancelled` - ConcreteState

```java
public class New implements OrderState {

    @Override
    public double handleCancellation() {
        System.out.println("It's a new Order. No processing done");
        return 0;
    }
}
```

```java
public class Paid implements OrderState {

    @Override
    public double handleCancellation() {
        System.out.println("Contacting payment gateway to rollback transaction.");
        return 10;
    }
}
```

```java
public class InTransit implements OrderState {

    @Override
    public double handleCancellation() {
        System.out.println("Contacting courier service for cancellation");
        System.out.println("Contacting payment gateway for transaction roll back");
        return 20;
    }
}
```

```java
public class Delivered implements OrderState {

    @Override
    public double handleCancellation() {
        System.out.println("Contacting courier service for item pickup");
        System.out.println("Payment roll back will be initiated upon receiving returned item");
        return 30;
    }
}
```

```java
public class Cancelled implements OrderState {

    @Override
    public double handleCancellation() {
        throw new IllegalStateException("Cancelled order. Can't cancel anymore");
    }
}
```

Всеки клас отговаря на въпроса "какво се случва при анулиране, ако сме в това състояние" - колкото "по-напреднало" е състоянието (по-близо до `Delivered`), толкова повече допълнителни действия (контакт с куриер, payment rollback) и по-висока такса за анулиране. `Cancelled` отказва повторно анулиране с exception.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        Order order = new Order();
        order.paymentSuccessful();
        order.dispatched();
        order.cancel();
    }
}
```

---

## Употреба

Трасиране на `Client.main`:

1. `new Order()` → `currentState = New`
2. `order.paymentSuccessful()` → `currentState = Paid` (директен преход, без да пита `New`)
3. `order.dispatched()` → `currentState = InTransit`
4. `order.cancel()` → `currentState.handleCancellation()` = `InTransit.handleCancellation()`:
   - извежда двата реда по-долу,
   - връща `20.0` (стойността не се отпечатва - `cancel()`-ът на `Client` я отхвърля),
   - `currentState = Cancelled`.

Изход:
```
Contacting courier service for cancellation
Contacting payment gateway for transaction roll back
```

---

## Pitfalls

- **Само `cancel()` минава през `OrderState`** - `paymentSuccessful()`/`dispatched()`/`delivered()` са твърдо закодирани преходи в `Order`, не делегирани/валидирани от `currentState`. Напр. `order.dispatched()` веднага след `order.cancel()` би презаписал `currentState` от `Cancelled` обратно на `InTransit` - без грешка, въпреки че логически е невалиден преход.
- **Връщаната такса (`charges`) от `cancel()` се губи** - `Client` не я отпечатва/използва никъде.
- **`Cancelled.handleCancellation()` хвърля `IllegalStateException`**, но `Client` не го хваща - втори `order.cancel()` би сринал програмата с необработен exception.
