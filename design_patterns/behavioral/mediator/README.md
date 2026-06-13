# Mediator Pattern

Mediator е **Behavioral** pattern, който централизира комуникацията между група обекти ("колеги") в един посредник. Колегите не държат референции едни към други - всеки знае само за медиатора, а той решава кои други колеги да извести при промяна.

В този пример - няколко UI контроли (`TextBox`, `Slider`, `Label`) споделят една и съща стойност. Когато една се промени, медиаторът известява останалите да се синхронизират.

---

## Кога се ползва?

Когато имаш група обекти, които трябва да реагират на промени едни в други, но:
- директни референции между всички би означавало N² връзки (всеки знае за всеки),
- искаш логиката "кой известява кого и как" да живее на **едно** място, а не разпръсната във всеки обект.

---

## Участници

| Роля | Клас |
|---|---|
| Colleague (interface) | `UIControl` |
| ConcreteMediator | `UIMediator` |
| ConcreteColleague | `TextBox`, `Slider`, `Label` |
| Client | `Client` |

---

## Имплементация

### `UIControl` - Colleague interface

```java
//Abstract colleague
public interface UIControl {

    void controlChanged(UIControl control);

    void setControlValue(String value);

    String getControlValue();

    String getControlName();
}
```

Единен договор - медиаторът работи само с този тип, не знае за `TextBox`/`Slider`/`Label` конкретно.

### `UIMediator` - ConcreteMediator

```java
//Mediator
public class UIMediator {

    private List<UIControl> colleagues = new ArrayList<>();

    public void register(UIControl control) {
        colleagues.add(control);
    }

    public void valueChanged(UIControl control) {
        colleagues.stream()
                .filter(c -> !c.equals(control))
                .forEach(c -> c.controlChanged(control));
    }
}
```

- `register` добавя нов колега в списъка - всеки колега се регистрира сам в конструктора си.
- `valueChanged(control)` се вика от колегата, който се е променил (`control` = подателят). Медиаторът обхожда всички **останали** регистрирани колеги (`filter` изключва подателя) и вика `controlChanged(control)` на всеки от тях.

### `TextBox` / `Slider` / `Label` - ConcreteColleague

```java
public class TextBox implements UIControl {

    private String text = "Lorem ipsum";
    private final UIMediator mediator;

    public TextBox(UIMediator mediator) {
        this.mediator = mediator;
        this.mediator.register(this);
    }

    @Override
    public void setControlValue(String text) {
        this.text = text;
        mediator.valueChanged(this);
    }

    @Override
    public void controlChanged(UIControl control) {
        this.text = control.getControlValue();
    }

    @Override
    public String getControlValue() {
        return text;
    }

    @Override
    public String getControlName() {
        return "TextBox";
    }
}
```

`Slider` и `Label` са структурно идентични (само `getControlName()` и default текста се различават).

- **Конструктор** - регистрира `this` при медиатора (`mediator.register(this)`).
- **`setControlValue(...)`** - вика се при "собствена" промяна (от Client-а) - обновява `text` и известява медиатора (`mediator.valueChanged(this)`).
- **`controlChanged(control)`** - вика се от медиатора, когато **друг** колега се е променил - директно "огледва" чуждата стойност (`this.text = control.getControlValue()`), без да известява медиатора отново.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        UIMediator mediator = new UIMediator();

        UIControl c1 = new Label(mediator);
        UIControl c2 = new TextBox(mediator);

        System.out.println(c2.getControlValue());
        c1.setControlValue("Hello");
        System.out.println(c2.getControlValue());
    }
}
```

---

## Употреба

Изход:
```
Lorem ipsum
Hello
```

`c2` (`TextBox`) тръгва с default стойност `"Lorem ipsum"`. `c1.setControlValue("Hello")` → `mediator.valueChanged(c1)` → медиаторът вика `controlChanged(c1)` на всеки друг регистриран колега (тук само `c2`) → `c2` "огледва" `c1.getControlValue()` и пренася `"Hello"` в собственото си `text`. `c1` не знае нито за `c2`, нито за неговия конкретен тип - само за `mediator`.

---

## Pitfalls

- **Без `mediatedUpdate` флаг, но безопасно** - `controlChanged` пише директно в `this.text`, без да вика `mediator.valueChanged(this)`. Затова веригата спира след първото ниво и няма риск от безкрайна рекурсия. Ако `controlChanged` беше извикал `setControlValue` (който **известява** медиатора), щеше да се получи `controlChanged` → `setControlValue` → `valueChanged` → `controlChanged` на всички → ... → безкраен loop - точно затова е важно `controlChanged` да остане "тих" мутатор.
- **`filter(c -> !c.equals(control))` ползва default `equals`** - `UIControl`-имплементациите нямат собствен `equals()`/`hashCode()`, значи `equals` е reference equality (`==`). Работи коректно, докато всеки колега е единствена инстанция - но е скрита зависимост от това, че никой не override-ва `equals`.
- **Регистрацията е поред в конструктора** - ако `valueChanged` се извика между създаването на два колеги (напр. в собствения конструктор на втория, преди `register`), първият няма да получи известие за втория, защото втория още не е в `colleagues` в момента на известяването.
- **`getControlValue()` връща `String` за всичко** - `Slider`-ът тук пази `text` като `String`, не число. Опростено е нарочно за демонстрация на pattern-а - реален slider би имал числова стойност и съответна конверсия.
