# Facade Pattern

Facade е **Structural** pattern, който предоставя **опростен интерфейс** към сложна подсистема от класове. Не крие подсистемата - просто добавя удобен "вход" за честите случаи.

---

## Кога се ползва?

Когато за **една обикновена задача** трябва да викаш **няколко класа**, в **точно определен ред**:

```java
Template template = TemplateFactory.createTemplateFor(TemplateType.Email);
Stationary stationary = StationaryFactory.createStationary();
Email email = Email.getBuilder()
              .withTemplate(template)
              .withStationary(stationary)
              .forObject(order)
              .build();
Mailer mailer = Mailer.getMailer();
mailer.send(email);
```

5 реда, 4 различни класа - за нещо, което клиентът мисли като "изпрати email за поръчка".

---

## Участници

| Роля | Клас |
|------|------|
| Facade | `EmailFacade` |
| Подсистема | `Template`, `TemplateFactory`, `Stationary`, `StationaryFactory`, `Email`, `EmailBuilder`, `Mailer` |
| Client | `Client` |

---

## Имплементация

### `EmailFacade` - опростява достъпа до подсистемата

```java
public class EmailFacade {

    public boolean sendOrderEmail(Order order) {
        Template template = TemplateFactory.createTemplateFor(TemplateType.Email);
        Stationary stationary = StationaryFactory.createStationary();
        Email email = Email.getBuilder()
                .withTemplate(template)
                .withStationary(stationary)
                .forObject(order)
                .build();
        Mailer mailer = Mailer.getMailer();
        return mailer.send(email);
    }
}
```

### Подсистемата (накратко)

- `Template` / `TemplateFactory` - избира шаблон според типа (`Email`, `NewsLetter`)
- `Stationary` / `StationaryFactory` - "канцеларски материали" (header/footer на писмото)
- `Email` / `EmailBuilder` - сглобява писмото (Builder pattern отвътре)
- `Mailer` - изпраща писмото (Singleton - `getMailer()`)

Всеки от тези класове **продължава да съществува самостоятелно** и може да се ползва директно.

---

## Употреба

### С Facade

```java
Order order = new Order("101", 99.99);

EmailFacade facade = new EmailFacade();
boolean result = facade.sendOrderEmail(order);
```

### Без Facade - същият резултат, но клиентът знае всички детайли

```java
Template template = TemplateFactory.createTemplateFor(TemplateType.Email);
Stationary stationary = StationaryFactory.createStationary();
Email email = Email.getBuilder()
              .withTemplate(template)
              .withStationary(stationary)
              .forObject(order)
              .build();
Mailer mailer = Mailer.getMailer();
boolean result = mailer.send(email);
```

И двата варианта са валидни - Facade е **допълнителен**, по-прост вход, не заменя подсистемата.

---

## Facade vs Adapter

| | Facade | Adapter |
|--|--------|---------|
| Брой класове отдолу | Много (цяла подсистема) | Един (adaptee) |
| Цел | Опростяване | Превод между несъвместими интерфейси |
| Нов интерфейс? | Не задължително - може да е изцяло нов, опростен | Винаги - имплементира конкретен очакван интерфейс |

---

## Pitfalls

- Facade **не трябва да добавя бизнес логика** - само координира извиквания към подсистемата
- Ако Facade започне да поддържа твърде много опции/параметри, рискуваш да пресъздадеш сложността, която си искал да скриеш
- Подсистемата остава достъпна директно - Facade е удобство, не enforcement
