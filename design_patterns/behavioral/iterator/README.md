# Iterator Pattern

Iterator е **Behavioral** pattern, който дава начин да обхождаш елементите на колекция **последователно**, без клиентският код да знае как колекцията е устроена отвътре.

---

## Кога се ползва?

Когато искаш да обходиш елементите на "агрегат" (колекция/enum/структура), но:
- не искаш клиентът да зависи от вътрешното представяне (масив, list, и т.н.),
- искаш единен начин за обхождане (`hasNext()` / `next()`), независимо от конкретната структура.

В този пример - `ThemeColor` е enum (агрегат) с фиксиран набор от стойности. Вместо клиентът да вика директно `ThemeColor.values()` и да обхожда масива, той получава `Iterator<ThemeColor>`.

---

## Участници

| Роля | Клас |
|---|---|
| Iterator (interface) | `Iterator<T>` |
| ConcreteIterator | `ThemeColor.ThemeColorIterator` |
| Aggregate | `ThemeColor` (статичен factory метод `getIterator()`) |
| Client | `Client` |

---

## Имплементация

### `Iterator<T>` - Iterator interface

```java
public interface Iterator<T> {

    boolean hasNext();

    T next();
}
```

### `ThemeColor` - Aggregate + ConcreteIterator

```java
public enum ThemeColor {

    RED,
    ORANGE,
    BLACK,
    WHITE;

    public static Iterator<ThemeColor> getIterator() {
        return new ThemeColorIterator();
    }

    private static class ThemeColorIterator implements Iterator<ThemeColor> {

        private int position;

        @Override
        public boolean hasNext() {
            return position < values().length;
        }

        @Override
        public ThemeColor next() {
            return values()[position++];
        }
    }
}
```

- `ThemeColorIterator` е **private static** вътрешен клас - извън `ThemeColor` никой не може директно да си направи нов `ThemeColorIterator`. Единственият вход е `ThemeColor.getIterator()`.
- `position` е **state на конкретния iterator**, не на `ThemeColor` - всеки извикан `getIterator()` връща нов обект с `position = 0`. Може да имаш няколко независими обхождания едновременно.
- `values()` е вграденият статичен метод на всеки `enum`, връщащ масив с всички константи по реда на деклариране (`[RED, ORANGE, BLACK, WHITE]`). `ThemeColorIterator` го извиква, понеже е nested в `ThemeColor`.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        Iterator<ThemeColor> iter = ThemeColor.getIterator();

        while (iter.hasNext()) {
            ThemeColor themeColor = iter.next();
            System.out.println(themeColor);
        }
    }

}
```

Изход:
```
RED
ORANGE
BLACK
WHITE
```

`Client` не знае, че зад `Iterator<ThemeColor>` стои `enum.values()` - би работило по същия начин, ако `ThemeColor` утре стане обикновен клас с `List<ThemeColor>` отвътре.

---

## Връзка с Java `Iterable`/`for-each`

Тук `Iterator<T>` е **собствен** interface (не `java.util.Iterator`), затова `ThemeColor` не може да се ползва директно с `for-each`. За да стане това възможно, `ThemeColor` би трябвало да implement-ва `java.lang.Iterable<ThemeColor>` с метод `iterator()`, връщащ `java.util.Iterator<ThemeColor>` - тогава `for (ThemeColor c : ThemeColor.values())`-стилът би работил и без явен `while`/`hasNext()`.

---

## Pitfalls

- `ThemeColorIterator.position` се увеличава **само напред** - няма `previous()`/reset; нов обход изисква нов `getIterator()`.
- Понеже `ThemeColor.values()` връща **нов масив при всяко извикване**, `hasNext()`/`next()` викат `values()` отново и отново - леко разхищение, но коректно (резултатът е стабилен, защото `enum` константите са фиксирани).
- Ако `Iterator<T>` тук се обърка с `java.util.Iterator` (еднакво име, различен пакет) - imports трябва да са изрични, иначе `import design_patterns.behavioral.iterator.Iterator` може да бъде засенчен от `java.util.Iterator` (същия import-shadowing проблем, който видяхме при Proxy).
