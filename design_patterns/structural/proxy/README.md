# Proxy Pattern

Proxy е **Structural** pattern, при който създаваш **заместител (surrogate)** на обект - имплементира **същия интерфейс** като реалния обект и стои "пред" него, контролирайки достъпа до него.

Клиентският код работи с proxy-то през интерфейса, без да знае дали говори директно с реалния обект или през "посредник".

---

## Кога се ползва?

Три класически случая:

| Тип | Цел |
|---|---|
| **Protection Proxy** | Контролира достъп до операциите на реалния обект (права/permissions) |
| **Remote Proxy** | Локален представител на отдалечен (remote) обект |
| **Virtual Proxy** | Забавя създаването на "скъп" обект до момента, в който наистина потрябва (lazy loading) |

Този пример демонстрира **Virtual Proxy** - реалният `BitmapImage` (симулира "зареждане от диск") се създава едва при първото извикване на `render()`.

---

## Участници

| Роля | Клас |
|------|------|
| Subject (interface) | `Image` |
| RealSubject | `BitmapImage` |
| Proxy (static) | `virtual.ImageProxy` |
| Proxy (dynamic) | генериран `$ProxyN` + `dynamic.ImageInvocationHandler` |
| Client | `virtual.Client`, `dynamic.Client` |

Проектът съдържа **два варианта** на същия pattern - **static proxy** (`virtual` пакет) и **dynamic proxy** (`dynamic` пакет), за сравнение.

---

## Споделени типове (`proxy`)

### `Image` - Subject интерфейс

```java
public interface Image {
    void setLocation(Point2D point2d);
    Point2D getLocation();
    void render();
}
```

И двата proxy-варианта (static и dynamic) implement-ват/проксират този интерфейс.

### `BitmapImage` - RealSubject

```java
public class BitmapImage implements Image {

    private Point2D location;
    private String name;

    public BitmapImage(String filename) {
        System.out.println("Loaded from disk:" + filename); // "скъпата" операция
        name = filename;
    }

    @Override
    public void setLocation(Point2D point2d) {
        location = point2d;
    }

    @Override
    public Point2D getLocation() {
        return location;
    }

    @Override
    public void render() {
        System.out.println("Rendered " + this.name);
    }
}
```

`System.out.println("Loaded from disk:...")` симулира бавна/скъпа операция (реално би било I/O към диска).

---

## Вариант 1: Static (Virtual) Proxy - пакет `virtual`

### `ImageProxy`

```java
public class ImageProxy implements Image {

    private BitmapImage image;   // null, докато не потрябва
    private String name;
    private Point2D location;    // "буфер" преди да съществува image

    public ImageProxy(String name) {
        this.name = name;        // НЕ създава BitmapImage тук!
    }

    @Override
    public void setLocation(Point2D point2d) {
        if (image != null) {
            image.setLocation(point2d);
        } else {
            location = point2d;  // запомняме, докато image не съществува
        }
    }

    @Override
    public Point2D getLocation() {
        if (image != null) {
            return image.getLocation();
        }
        return location;
    }

    @Override
    public void render() {
        if (image == null) {
            image = new BitmapImage(name);   // ← реалният обект се създава ТУК
            if (location != null) {
                image.setLocation(location); // прехвърляме запомнената позиция
            }
        }
        image.render();
    }
}
```

### `ImageFactory`

```java
public class ImageFactory {
    public static Image getImage(String name) {
        return new ImageProxy(name);
    }
}
```

### Употреба (`virtual.Client`)

```java
Image img = ImageFactory.getImage("A1.bmp");

img.setLocation(new Point2D(10, 10));
System.out.println("Image location: " + img.getLocation());
System.out.println("rendering image now...");
img.render();
```

Изход:
```
Image location: Point2D [x=10.0, y=10.0]
rendering image now...
Loaded from disk:A1.bmp
Rendered A1.bmp
```

`"Loaded from disk:A1.bmp"` се появява **едва при `render()`** - не при `getImage()` или `setLocation()`.

---

## Вариант 2: Dynamic Proxy - пакет `dynamic`

Вместо да пишеш конкретен клас `implements Image`, JVM-ът **генерира** proxy клас по време на изпълнение. Ти пишеш само `InvocationHandler`, чийто `invoke()` се вика при всяко повикване на метод от `Image`.

### `ImageInvocationHandler`

```java
public class ImageInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method setLocationMethod = Image.class.getMethod("setLocation", new Class[]{Point2D.class});
        if (setLocationMethod.equals(method)) {
            Point2D point2d = (Point2D) args[0];
            System.out.println("From InvocationHandler: " + point2d);
        }
        return null;
    }
}
```

- `Image.class.getMethod("setLocation", new Class[]{Point2D.class})` - reflection lookup на `Method` обекта, описващ `setLocation(Point2D)`.
- `setLocationMethod.equals(method)` - проверка кой метод реално е извикан на проксирания обект.
- За `setLocation` - принтира подадената точка. За `getLocation`/`render` - връща `null` (т.е. не прави нищо).

### `ImageFactory`

```java
public class ImageFactory {
    public static Image getImage() {
        return (Image) Proxy.newProxyInstance(
                ImageFactory.class.getClassLoader(),
                new Class[] {Image.class},
                new ImageInvocationHandler());
    }
}
```

- `Proxy.newProxyInstance(loader, interfaces, handler)` създава нов клас (`$Proxy0`), който implement-ва `Image`, без ти да пишеш такъв клас.

### Употреба (`dynamic.Client`)

```java
Image img = ImageFactory.getImage();
img.setLocation(new Point2D(-10, 0));
```

Изход:
```
From InvocationHandler: Point2D [x=-10.0, y=0.0]
```

---

## Static vs Dynamic Proxy

| | Static (`virtual`) | Dynamic (`dynamic`) |
|---|---|---|
| Кога се създава proxy класът | Compile time - ти пишеш `ImageProxy` | Runtime - JVM генерира `$ProxyN` |
| Брой класове | 1 proxy клас на 1 интерфейс | 1 `InvocationHandler` за произволен интерфейс |
| Type safety | Пълна | Чрез `Method`/reflection |
| Изисквания | Имплементираш интерфейса ръчно | Изисква **интерфейс** (не работи директно с конкретни класове) |
| Типичен пример | Ръчно писан `ImageProxy` | Spring AOP, Mockito, Hibernate lazy-loading proxies |

---

## Pitfalls

- **Import shadowing**: explicit `import` за клас със същото име като друг (`java.awt.Image` срещу твоя `Image`) "засенчва" wildcard import-а - грешният избор води до `NoSuchMethodException` при `getMethod(...)`.
- **`UndeclaredThrowableException`**: ако `InvocationHandler.invoke()` хвърли checked exception, който интерфейсният метод не декларира с `throws`, JVM-ът го опакова в `UndeclaredThrowableException`.
- **Encapsulation**: за да работи Proxy коректно, клиентът не трябва да може да направи `new BitmapImage(...)` директно (package-private конструктор/клас или factory) - иначе заобикаля lazy-loading/protection логиката.
- Двата `ImageFactory` класа (в `virtual` и `dynamic`) имат **еднакво име** - ако `Client` импортира грешния, ще извика грешната имплементация без compile error (виж разликата в сигнатурата - `getImage(String)` срещу `getImage()`).
