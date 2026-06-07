# Object Pool Pattern

Object Pool е **Creational** pattern, който **преизползва обекти** вместо да ги създава и унищожава всеки път. Подходящ е, когато създаването на обект е скъпо.

---

## Кога се ползва?

Когато имаш скъпи обекти, които се ползват временно и могат да се върнат обратно:
- Database connections
- Thread pools
- Bitmap/Image обекти в игри

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Poolable | `Poolable` |
| Abstract Product | `Image` |
| Concrete Product | `Bitmap` |
| Pool | `ObjectPool<T>` |
| Client | `Client` |

---

## Имплементация

### `Poolable` - всеки обект в пула трябва да може да се нулира

```java
public interface Poolable {
    void reset();
}
```

### `Image` - абстрактен продукт

```java
public interface Image extends Poolable {
    void draw();
    Point2D getLocation();
    void setLocation(Point2D location);
}
```

### `Bitmap` - конкретен продукт

```java
public class Bitmap implements Image {
    private Point2D location;
    private final String name;

    @Override
    public void reset() {
        location = null; // нулира само стейта, не името
    }
}
```

### `ObjectPool` - управлява пула

```java
public class ObjectPool<T extends Poolable> {
    private BlockingQueue<T> availablePool;

    public ObjectPool(Supplier<T> creator, int count) {
        availablePool = new LinkedBlockingQueue<>();
        for (int i = 0; i < count; i++) {
            availablePool.offer(creator.get());
        }
    }

    public T get() {
        try {
            return availablePool.take(); // блокира, ако пулът е празен
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // възстановява interrupted статуса
            System.err.println("take() was interrupted");
        }
        return null;
    }

    public void release(T obj) {
        obj.reset();
        try {
            availablePool.put(obj); // блокира, ако пулът е пълен
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("put() was interrupted");
        }
    }
}
```

### `Client`

```java
private static final ObjectPool<Bitmap> bitmapPool =
        new ObjectPool<>(() -> new Bitmap("Logo.bmp"), 5);

Bitmap b1 = bitmapPool.get();       // вземи от пула
b1.setLocation(new Point2D(10, 10));
b1.draw();
bitmapPool.release(b1);             // върни в пула - reset() се вика автоматично
```

---

## BlockingQueue методи

| | Блокира | Връща false/null | Exception |
|--|---------|-----------------|-----------|
| Добавяне | `put()` | `offer()` | `add()` |
| Вземане | `take()` | `poll()` | `remove()` |

---

## Pitfalls

- Обектът трябва да е **правилно нулиран** при `release()` - иначе, следващият клиент получава замърсен стейт
- Пулът трябва да е **thread-safe** - `BlockingQueue` го гарантира
- Ако нишката е прекъсната, трябва да се възстанови `interrupted` флагът
