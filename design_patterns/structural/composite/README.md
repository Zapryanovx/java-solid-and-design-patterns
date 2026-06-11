# Composite Pattern

Composite е **Structural** pattern, който позволява **отделни обекти** и **групи от обекти** да се третират **еднакво**, чрез общ интерфейс. Подходящ е за **дървовидни структури**.

---

## Кога се ползва?

Когато имаш **дървовидна структура**, в която и "листата", и "клоните" трябва да поддържат **една и съща операция**:

```
dir2
├── file2        (2000)
├── file3        (150)
└── dir1
    └── File1    (1000)
```

`dir2.ls()` трябва да изведе **себе си и всичко вътре**, независимо колко нива надолу.

---

## Участници

| Роля | Клас |
|------|------|
| Component | `File` |
| Leaf | `BinaryFile` |
| Composite | `Directory` |
| Client | `Client` |

---

## Имплементация

### `File` - Component

```java
public abstract class File {

    private String name;

    public File(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void ls();

    public abstract void addFile(File file);

    public abstract File[] getFiles();

    public abstract boolean removeFile(File file);
}
```

Дефинира операции, общи за **двата вида** - дори `addFile`/`getFiles`/`removeFile`, които **само Composite** реално поддържа.

---

### `BinaryFile` - Leaf

```java
public class BinaryFile extends File {

    private final long size;

    public BinaryFile(String name, long size) {
        super(name);
        this.size = size;
    }

    @Override
    public void ls() {
        System.out.println(getName() + "\t" + size);
    }

    @Override
    public void addFile(File file) {
        throw new UnsupportedOperationException("Leaf node doesn't support add operation.");
    }

    @Override
    public File[] getFiles() {
        throw new UnsupportedOperationException("Leaf node doesn't support getFiles operation.");
    }

    @Override
    public boolean removeFile(File file) {
        throw new UnsupportedOperationException("Leaf node doesn't support remove operation.");
    }
}
```

`BinaryFile` имплементира `addFile`/`getFiles`/`removeFile`, само за да хвърли изключение - тези операции **нямат смисъл** за лист.

---

### `Directory` - Composite

```java
public class Directory extends File {

    private List<File> children = new ArrayList<>(); // -children

    public Directory(String name) {
        super(name);
    }

    @Override
    public void ls() {
        System.out.println(getName());
        children.forEach(File::ls); // делегира на всяко дете
    }

    @Override
    public void addFile(File file) {
        children.add(file);
    }

    @Override
    public File[] getFiles() {
        return children.toArray(new File[children.size()]);
    }

    @Override
    public boolean removeFile(File file) {
        return children.remove(file);
    }
}
```

`ls()` извиква `ls()` на всяко дете - независимо дали е `BinaryFile` или друга `Directory`. Рекурсията се случва "сама" - `Directory.ls()` извиква `child.ls()`, а ако `child` е `Directory`, пак влиза в същия метод.

---

## Употреба

```java
File file1 = new BinaryFile("File1", 1000);
Directory dir1 = new Directory("dir1");
dir1.addFile(file1);

File file2 = new BinaryFile("file2", 2000);
File file3 = new BinaryFile("file3", 150);

Directory dir2 = new Directory("dir2");
dir2.addFile(file2);
dir2.addFile(file3);
dir2.addFile(dir1); // Directory вътре в Directory

dir2.ls();
```

---

## Изход

```
dir2
file2	2000
file3	150
dir1
File1	1000
```

---

## Pitfalls

- `BinaryFile` (Leaf) трябва да хвърля `UnsupportedOperationException` за `addFile`/`getFiles`/`removeFile` - "цената" на еднаквия интерфейс е методи, които нямат смисъл за листата
- Клиентът може погрешно да извика `addFile()` на `BinaryFile` и да получи runtime грешка вместо compile-time - алтернатива е `add`/`remove`/`getChildren` да са само в `Composite`, но тогава клиентът трябва да прави `instanceof` проверки
- Рекурсията (`ls()` → `ls()` на децата) работи "автоматично" - не пишеш цикли за всяко ниво на дървото
