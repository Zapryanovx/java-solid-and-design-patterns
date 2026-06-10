# Bridge Pattern

Bridge е **Structural** pattern, който разделя **абстракцията** от **имплементацията**, така че двете да могат да се променят независимо едно от друго - без `m × n` комбинации от класове.

---

## Кога се ползва?

Когато имаш **две независими измерения**, които иначе би трябвало да комбинираш в подкласове:

```
FifoCollection (какво прави) - offer(), poll()
LinkedList (как съхранява)   - addFirst(), removeFirst(), addLast(), removeLast()
```

Без Bridge, за всяка комбинация колекция/съхранение би трябвало нов клас. С Bridge - линейно разширяване.

---

## Участници

| Роля | Клас |
|------|------|
| Abstraction | `FifoCollection<T>` |
| Refined Abstraction | `Queue<T>` |
| Implementor | `LinkedList<T>` |
| Concrete Implementor | `SinglyLinkedList<T>`, `ArrayLinkedList<T>` |
| Client | `Client` |

---

## Имплементация

### `FifoCollection` - Abstraction

```java
public interface FifoCollection<T> {
    void offer(T element); // добавя елемент
    T poll();               // връща и премахва първия елемент
}
```

### `LinkedList` - Implementor

```java
public interface LinkedList<T> {
    void addFirst(T element);
    T removeFirst();
    void addLast(T element);
    T removeLast();
    int getSize();
}
```

`LinkedList` е **отделна йерархия**, несвързана с `FifoCollection` - дефинира примитивни операции за съхранение.

---

### `Queue` - Refined Abstraction

```java
public class Queue<T> implements FifoCollection<T> {

    private LinkedList<T> list; // мостът

    public Queue(LinkedList<T> list) {
        this.list = list;
    }

    @Override
    public void offer(T element) {
        list.addFirst(element); // делегира към Implementor
    }

    @Override
    public T poll() {
        return list.removeFirst();
    }
}
```

### `SinglyLinkedList` и `ArrayLinkedList` - Concrete Implementors

Две различни реализации на `LinkedList<T>`:

- `SinglyLinkedList` - класически linked list с node-ове
- `ArrayLinkedList` - реализация чрез масив

И двете имплементират **един и същи интерфейс** - `Queue` не знае и не го интересува коя точно ползва.

---

## Употреба

```java
FifoCollection<Integer> collection = new Queue<>(new SinglyLinkedList<>());

collection.offer(10);
collection.offer(40);
collection.offer(99);

System.out.println(collection.poll());
System.out.println(collection.poll());
System.out.println(collection.poll());
System.out.println(collection.poll()); // празна колекция
```

Смяна на имплементацията **без да пипаш `Queue`**:

```java
FifoCollection<Integer> collection = new Queue<>(new ArrayLinkedList<>());
```

---

## Защо `Queue.offer()` приема `LinkedList`, а не конкретен клас

```java
public Queue(LinkedList<T> list) {
    this.list = list; // тип LinkedList, не SinglyLinkedList
}
```

Полето `list` е декларирано като `LinkedList<T>` (интерфейса). Ако `SinglyLinkedList` добави нов метод, който го няма в `LinkedList`, `Queue` няма да може да го извика - типът на полето "отрязва" достъпа. Това е **цената**, която плащаш за взаимозаменяемостта.

---

## Bridge vs Decorator

| | Bridge | Decorator |
|--|--------|-----------|
| Интерфейси | Различни (`FifoCollection` ≠ `LinkedList`) | Еднакви (Decorator = Component) |
| Комбиниране | Веднъж, при създаване | Произволни слоеве, по време на изпълнение |
| Цел | Избягва `m × n` класове | Добавя поведение динамично |

---

## Pitfalls

- Implementor трябва да съдържа **примитивни** операции - Abstraction ги комбинира в смислени операции
- Полето към Implementor винаги е от тип **интерфейс**, не конкретен клас - това е условието за взаимозаменяемост
- Refined Abstraction може свободно да добавя нови методи - клиентът избира по-конкретния тип, ако му трябват
