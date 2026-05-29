# ISP - Рефакторинг

Проблемният `Animal` интерфейс нарушаваше ISP, защото принуждаваше класовете да имплементират методи, които не им принадлежат.

---

## Решение - един голям интерфейс → много малки

### Интерфейси

```java
public interface Eatable  { void eat(); }
public interface Drinkable { void drink(); }
public interface Sleepable { void sleep(); }
public interface Swimmable { void swim(); }
public interface Flyable   { void fly(); }
```

### `Dog` - имплементира само това което му трябва

```java
public class Dog implements Eatable, Drinkable, Sleepable, Swimmable {

    @Override
    public void eat()   { System.out.println("Eating Dog"); }

    @Override
    public void drink() { System.out.println("Drinking Dog"); }

    @Override
    public void sleep() { System.out.println("Sleeping Dog"); }

    @Override
    public void swim()  { System.out.println("Swimming Dog"); }
}
```

### `Eagle` - имплементира само това което му трябва

```java
public class Eagle implements Eatable, Drinkable, Sleepable, Flyable {

    @Override
    public void eat()   { System.out.println("Eating Eagle"); }

    @Override
    public void drink() { System.out.println("Drinking Eagle"); }

    @Override
    public void sleep() { System.out.println("Sleeping Eagle"); }

    @Override
    public void fly()   { System.out.println("Flying Eagle"); }
}
```

---

## Резултат

| | Проблемен код | След рефакторинг |
|--|---------------|-----------------|
| Интерфейс | Един `Animal` с всичко | 5 малки специфични интерфейса |
| `Dog` | Принуден да имплементира `fly()` | Само `Eatable, Drinkable, Sleepable, Swimmable` |
| `Eagle` | Принуден да имплементира `swim()` | Само `Eatable, Drinkable, Sleepable, Flyable` |
| Нов метод | Всички класове трябва да го имплементират | Само класовете, за които има смисъл |
| ISP | Нарушен | Спазен |
