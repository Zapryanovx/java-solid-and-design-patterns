# Strategy Pattern

Strategy е **Behavioral** pattern, при който се изнася **семейство от взаимозаменяеми алгоритми** зад общ интерфейс. Контекстният клас не знае *как* точно се извършва операцията - той просто делегира на текущо инжектираната "стратегия".

В този пример `PrintService` (Context) трябва да отпечата списък от поръчки, но **начинът** на отпечатване (детайлен отчет по артикули или само обобщение) се избира отвън, чрез подаване на конкретна имплементация на `OrderPrinter`.

---

## Кога се ползва?

Когато:
- имаш няколко начина да направиш едно и също нещо (тук: "отпечатай поръчки"), и искаш да можеш да ги превключваш без `if`/`switch`,
- искаш добавянето на нов вариант да става чрез **нов клас**, без да пипаш съществуващия Context.

---

## Участници

| Роля | Клас |
|---|---|
| Strategy (интерфейс) | `OrderPrinter` |
| ConcreteStrategy | `DetailPrinter`, `SummaryPrinter` |
| Context | `PrintService` |
| Client | `Client` |

`Order` е спомагателен модел (данните, върху които стратегиите работят) - не е самостоятелна роля в самия pattern.

---

## Имплементация

### `OrderPrinter` - Strategy

```java
//Strategy
public interface OrderPrinter {

    void print(Collection<Order> orders);
}
```

Общ договор за всички "печатащи" стратегии - получават колекция от поръчки и сами решават как да изведат отчета.

### `DetailPrinter` - ConcreteStrategy

```java
public class DetailPrinter implements OrderPrinter {

    @Override
    public void print(Collection<Order> orders) {
        System.out.println("************* Detail Report ***********");
        Iterator<Order> iter = orders.iterator();
        double total = 0;
        for (int i = 1; iter.hasNext(); i++) {
            double orderTotal = 0;
            Order order = iter.next();
            System.out.println(i + ". " + order.getId() + " \t" + order.getDate());
            for (Map.Entry<String, Double> entry : order.getItems().entrySet()) {
                System.out.println("\t\t" + entry.getKey() + "\t" + entry.getValue());
                orderTotal += entry.getValue();
            }
            System.out.println("----------------------------------------");
            System.out.println("\t\t Total  " + orderTotal);
            System.out.println("----------------------------------------");
            total += orderTotal;
        }
        System.out.println("----------------------------------------");
        System.out.println("\tGrand Total " + total);
    }
}
```

За всяка поръчка отпечатва ID, дата, всеки артикул с цената му, междинен сбор (`orderTotal`), а накрая - обща сума за всички поръчки (`total`).

### `SummaryPrinter` - ConcreteStrategy

```java
//Concrete strategy
public class SummaryPrinter implements OrderPrinter {

    @Override
    public void print(Collection<Order> orders) {
        System.out.println("************** Summary Report **************");

        Iterator<Order> iterator = orders.iterator();
        double total = 0;

        for (int i = 1; iterator.hasNext(); i++) {
            Order order = iterator.next();
            total += order.getTotal();

            System.out.println(
                    i + ". "
                    + order.getId() + "\t"
                    + order.getDate() + "\t"
                    + order.getItems().size() + "\t"
                    + order.getTotal()
            );

        }
        System.out.println("********************************");
        System.out.println("\t\t\t Total: " + total);
    }
}
```

Не влиза в детайли по артикули - за всяка поръчка показва само ID, дата, брой артикули и `getTotal()`, плюс обща сума накрая.

### `PrintService` - Context

```java
//Context
public class PrintService {

    private final OrderPrinter printer;

    public PrintService(OrderPrinter printer) {
        this.printer = printer;
    }

    public void printOrders(LinkedList<Order> orders) {
        printer.print(orders);
    }
}
```

- **`printer`** - текущо избраната стратегия, инжектирана през конструктора и неизменна (`final`) след това.
- **`printOrders`** - `PrintService` не съдържа никаква логика за форматиране - просто делегира на `printer.print(orders)`. За да смени отчета (детайлен ↔ обобщен), не се пипа `PrintService` - подава се друга имплементация на `OrderPrinter`.

### `Order` - модел

```java
public class Order {

    private final String id;

    private final LocalDate date;

    private final Map<String, Double> items = new HashMap<>();

    private double total;

    public Order(String id) {
        this.id = id;
        date = LocalDate.now();
    }

    public void addItem(String name, double price) {
        items.put(name, price);
        total += price;
    }

    // getId(), getDate(), getItems(), getTotal(), setTotal()
}
```

Всяка поръчка пази артикулите си в `Map<String, Double>` (име → цена) и текущ сбор `total`, обновяван автоматично при всяко `addItem`.

### `Client`

```java
public class Client {

    private static final LinkedList<Order> orders = new LinkedList<>();

    public static void main(String[] args) {
        createOrders();
        //print all orders
        PrintService service = new PrintService(new DetailPrinter());
        service.printOrders(orders);
    }

    private static void createOrders() {
        Order o = new Order("100");
        o.addItem("Soda", 2);
        o.addItem("Chips", 10);
        orders.add(o);

        o = new Order("200");
        o.addItem("Cake", 20);
        o.addItem("Cookies", 5);
        orders.add(o);

        o = new Order("300");
        o.addItem("Burger", 8);
        o.addItem("Fries", 5);
        orders.add(o);
    }
}
```

`Client` решава **коя стратегия** да инжектира - тук `new DetailPrinter()`. Замяната с `new SummaryPrinter()` би сменила целия формат на изхода, без да се променя нито `PrintService`, нито `Order`.

---

## Употреба

Трасиране на `Client.main` (с `DetailPrinter`):

1. `createOrders()` пълни `orders` с 3 поръчки: `100` (Soda=2, Chips=10 → total=12), `200` (Cake=20, Cookies=5 → total=25), `300` (Burger=8, Fries=5 → total=13).
2. `new PrintService(new DetailPrinter())` - Context-ът получава детайлна стратегия.
3. `service.printOrders(orders)` → `printer.print(orders)` → `DetailPrinter.print(orders)`.

Изход (датата е примерна - `LocalDate.now()` в деня на изпълнение):

```
************* Detail Report ***********
1. 100 	2026-06-15
		Chips	10.0
		Soda	2.0
----------------------------------------
		 Total  12.0
----------------------------------------
2. 200 	2026-06-15
		Cookies	5.0
		Cake	20.0
----------------------------------------
		 Total  25.0
----------------------------------------
3. 300 	2026-06-15
		Fries	5.0
		Burger	8.0
----------------------------------------
		 Total  13.0
----------------------------------------
----------------------------------------
	Grand Total 50.0
```

Ако `Client` беше подал `new SummaryPrinter()` вместо `new DetailPrinter()`, изходът щеше да е по 1 ред на поръчка (ID, дата, брой артикули, обща сума за поръчката) плюс обща сума накрая - без да се отпечатват отделните артикули.

---

## Pitfalls

- **Редът на артикулите вътре в поръчка не следва реда на `addItem`** - `items` е `HashMap`, така че "Chips" се отпечатва преди "Soda" въпреки че е добавен втори. Реда се определя от hash-овете на ключовете, не от вмъкването.
- **Изборът на стратегия е hardcode-нат в `Client`** (`new DetailPrinter()`) - смяната на отчет изисква промяна и рекомпилация на `Client`, няма механизъм за избор по конфигурация/в реално време.
- **`User.java`** е празен, неизползван клас - не участва в Strategy примера.
