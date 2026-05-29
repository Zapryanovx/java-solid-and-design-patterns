# Interface Segregation Principle (ISP)

> "Clients should not be forced to depend on interfaces they do not use."
> - Robert C. Martin

По-добре **много малки специфични интерфейси** отколкото **един голям общ**. Класовете не трябва да имплементират методи, които не им трябват.

## Нарушение

`Animal` интерфейсът съдържа `eat()`, `drink()`, `sleep()`, `swim()` и `fly()`. `Dog` е принуден да имплементира `fly()`, а `Eagle` - `swim()`, въпреки че нито едното не може.

```java
public void fly() {
    throw new UnsupportedOperationException("Dog cannot fly."); // ❌
}
```

## Решение

`Animal` се разбива на 5 малки интерфейса. Всеки клас имплементира **само това което му трябва**:

| Клас | Имплементира |
|------|-------------|
| `Dog` | `Eatable, Drinkable, Sleepable, Swimmable` |
| `Eagle` | `Eatable, Drinkable, Sleepable, Flyable` |

## Структура

```
problematic_snippet/   - един голям Animal интерфейс
solution/              - 5 малки специфични интерфейса
```
