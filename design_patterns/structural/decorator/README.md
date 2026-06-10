# Decorator Pattern

Decorator е **Structural** pattern, който добавя **допълнително поведение** на обект динамично, чрез "увиване" в друг обект със **същия интерфейс** - без подкласове за всяка комбинация.

---

## Кога се ползва?

Когато искаш да комбинираш поведения **по време на изпълнение**, в произволен брой и ред:

```java
Message m = new Base64EncodedMessage(
                new HtmlEncodedMessage(
                    new TextMessage("The <FORCE> is strong with this one!")));
```

Без Decorator би трябвало подклас за всяка комбинация: `HtmlMessage`, `Base64Message`, `HtmlBase64Message`, ...

---

## Участници

| Роля | Клас |
|------|------|
| Component | `Message` |
| Concrete Component | `TextMessage` |
| Decorator | `HtmlEncodedMessage`, `Base64EncodedMessage` |
| Client | `Client` |

---

## Имплементация

### `Message` - Component

```java
public interface Message {
    String getContent();
}
```

### `TextMessage` - Concrete Component

```java
public class TextMessage implements Message {
    private String msg;

    public TextMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String getContent() {
        return msg;
    }
}
```

---

### `HtmlEncodedMessage` - Decorator

```java
public class HtmlEncodedMessage implements Message {

    private Message msg; // -comp от диаграмата

    public HtmlEncodedMessage(Message msg) {
        this.msg = msg;
    }

    @Override
    public String getContent() {
        return escapeHtml(msg.getContent()); // делегира + добавя поведение
    }

    private String escapeHtml(String content) {
        return content
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
```

### `Base64EncodedMessage` - Decorator

```java
public class Base64EncodedMessage implements Message {

    private Message msg;

    public Base64EncodedMessage(Message msg) {
        this.msg = msg;
    }

    @Override
    public String getContent() {
        return Base64.getEncoder().encodeToString(msg.getContent().getBytes());
    }
}
```

---

## Употреба - увиване в увиване

```java
Message m = new TextMessage("The <FORCE> is strong with this one!");
System.out.println(m.getContent());
// The <FORCE> is strong with this one!

m = new HtmlEncodedMessage(m);
System.out.println(m.getContent());
// The &lt;FORCE&gt; is strong with this one!

m = new Base64EncodedMessage(m);
System.out.println(m.getContent());
// VGhlICZsdDtGT1JDRSZndDsgaXMgc3Ryb25nIHdpdGggdGhpcyBvbmUh
```

Всеки слой **не знае** какво има отдолу - само вика `msg.getContent()` и добавя своето отгоре.

---

## Bridge vs Decorator

| | Bridge | Decorator |
|--|--------|-----------|
| Интерфейси | Различни (Abstraction ≠ Implementor) | Еднакви (Decorator = Component) |
| Комбиниране | Веднъж, при създаване | Произволни слоеве, по време на изпълнение |
| Цел | Избягва `m × n` класове | Добавя поведение динамично |

---

## Pitfalls

- **Редът на decorator-ите има значение** - `Base64(Html(text))` ≠ `Html(Base64(text))`
- Всеки decorator трябва да имплементира **същия интерфейс** като компонента, за да може да се увива в друг decorator
- Прекалено много слоеве затрудняват debug-ването - стек trace-ът минава през всеки decorator
