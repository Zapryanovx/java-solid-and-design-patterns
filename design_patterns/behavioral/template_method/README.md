# Template Method Pattern

Template Method е **Behavioral** pattern, в който абстрактен базов клас дефинира **скелета на алгоритъм** като фиксирана последователност от стъпки, а конкретните подкласове имплементират стъпките - но не могат да променят реда им.

В този пример `OrderPrinter` заключва реда: `start → formatOrderNumber → formatItems → formatTotal → end`. `HtmlPrinter` и `TextPrinter` решават **какво** прави всяка стъпка (HTML тагове или обикновен текст), докато резултатът винаги се записва в **файл** от базовия клас.

---

## Кога се ползва?

Когато:
- имаш фиксиран процес (задължителен ред на стъпките), но **начинът** на изпълнение на всяка стъпка трябва да варира,
- искаш добавянето на нов вариант да се прави чрез **нов подклас**, без да пипаш алгоритъма в базовия клас.

---

## Участници

| Роля | Клас |
|---|---|
| AbstractClass (Template Method) | `OrderPrinter` |
| ConcreteClass | `HtmlPrinter`, `TextPrinter` |
| Client | `Client` |

---

## Имплементация

### `OrderPrinter` - AbstractClass

```java
//Abstract base class defines the template method
public abstract class OrderPrinter {

    public final void printOrder(Order order, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(start());
            out.println(formatOrderNumber(order));
            out.println(formatItems(order));
            out.println(formatTotal(order));
            out.println(end());
        }
    }

    protected abstract String start();

    protected abstract String formatOrderNumber(Order order);

    protected abstract String formatItems(Order order);

    protected abstract String formatTotal(Order order);

    protected abstract String end();
}
```

- **`printOrder`** е template method-ът - **`final`**, за да не може подклас да промени реда на стъпките. Той контролира и изхода (запис в `PrintWriter` към файл) - подкласовете само връщат `String`, не пишат директно.
- Петте абстрактни метода са `protected` - видими само за подкласовете, не за Client.

### `HtmlPrinter` - ConcreteClass

```java
// Concrete implementation. Implements steps needed by template method
public class HtmlPrinter extends OrderPrinter {

    @Override
    protected String start() {
        return "<html><head><title>Order Details</title></head><body>";
    }

    @Override
    protected String formatOrderNumber(Order order) {
        return "<h1>Order #" + order.getId() + "</h1>";
    }

    @Override
    protected String formatItems(Order order) {
        StringBuilder builder = new StringBuilder("<p><ul>");
        for (Map.Entry<String, Double> e : order.getItems().entrySet()) {
            builder.append("<li>" + e.getKey() + " $" + e.getValue() + "</li>");
        }
        builder.append("</ul></p>");
        return builder.toString();
    }

    @Override
    protected String formatTotal(Order order) {
        return "<br/><hr/><h3>Total : $" + order.getTotal() + "</h3>";
    }

    @Override
    protected String end() {
        return "</body></html>";
    }
}
```

Всяка стъпка обгражда стойностите с HTML тагове. `formatItems` итерира `order.getItems()` и генерира `<li>` за всеки артикул.

### `TextPrinter` - ConcreteClass

```java
//Concrete implementation. Implements steps needed by template method
public class TextPrinter extends OrderPrinter {

    @Override
    protected String start() {
        return "Order Details";
    }

    @Override
    protected String formatOrderNumber(Order order) {
        return "Order #" + order.getId();
    }

    @Override
    protected String formatItems(Order order) {
        StringBuilder builder = new StringBuilder("Items\n");
        for (Map.Entry<String, Double> entry : order.getItems().entrySet()) {
            builder.append(entry.getKey()).append(" $").append(entry.getValue()).append("\n");
        }
        return builder.append("--------------------------").toString();
    }

    @Override
    protected String formatTotal(Order order) {
        return "Total: $" + order.getTotal();
    }

    @Override
    protected String end() {
        return "";
    }
}
```

Същите 5 стъпки, но plain text форматиране. `end()` връща `""` - няма нужда от затварящ таг.

### `Client`

```java
public class Client {

    public static void main(String[] args) throws IOException {
        Order order = new Order("1001");
        order.addItem("Soda", 2.50);
        order.addItem("Sandwich", 11.95);
        order.addItem("Pizza", 15.95);

        OrderPrinter printer = new HtmlPrinter();
        printer.printOrder(order, "design_patterns/behavioral/template_method/1001.txt");
    }
}
```

Client избира конкретния подклас (`HtmlPrinter`) и подава пътя към изходния файл. За да смени формата на отчета, достатъчно е `new TextPrinter()` на ред 13 - без друга промяна.

---

## Употреба

Трасиране на `Client.main`:

1. Създава `Order "1001"` с артикули Soda (2.5), Sandwich (11.95), Pizza (15.95) → `total = 30.4`.
2. `printer.printOrder(order, "1001.txt")` → `OrderPrinter.printOrder` (template method) отваря `PrintWriter` към файла и извиква стъпките на `HtmlPrinter`.

Съдържание на `1001.txt` след изпълнение:

```
<html><head><title>Order Details</title></head><body>
<h1>Order #1001</h1>
<p><ul><li>Sandwich $11.95</li><li>Pizza $15.95</li><li>Soda $2.5</li></ul></p>
<br/><hr/><h3>Total : $30.4</h3>
</body></html>
```

Ако Client беше използвал `new TextPrinter()`, същият `printOrder` би записал:

```
Order Details
Order #1001
Items
Sandwich $11.95
Pizza $15.95
Soda $2.5
--------------------------
Total: $30.4

```

---

## Pitfalls

- **Редът на артикулите не следва реда на `addItem`** - `Order.items` е `HashMap`, итерацията върви по bucket-ите, не по вмъкването. Ако е важен редът, трябва `LinkedHashMap`.
- **Наследяване вместо композиция** - Template Method обвързва ConcreteClass с базовия клас за цял живот. За разлика от Strategy, не можеш да смениш алгоритъма runtime на вече създаден обект.
- **`TextPrinter.end()` връща `""`** - `out.println("")` записва допълнителен празен ред в края на файла.
- **Изходът е само файл** - `printOrder` не print-ва в конзолата, а директно в `PrintWriter`. Ако файловият път не съществува, се хвърля `FileNotFoundException` (подклас на `IOException`, който `main` декларира с `throws`).
