# Liskov Substitution Principle (LSP)

> "If S is a subtype of T, then objects of type T may be replaced with objects of type S without altering the correctness of the program."
> - Barbara Liskov

Навсякъде където използваш даден клас, трябва да можеш да го замениш с негов наследник - **без програмата да се държи неочаквано**.

## Нарушение

`Square extends Rectangle` и override-ва setters-ите за да поддържа равни страни. При заместване на `Rectangle` със `Square` резултатът е различен:

```
Rectangle: setWidth(10), setHeight(5) → area = 50 ✅
Square:    setWidth(10), setHeight(5) → area = 25 ❌
```

## Решение

`Rectangle` и `Square` стават **независими класове** - никой не наследява никого. И двата имплементират общия интерфейс `Shape`.

| Клас | Роля |
|------|------|
| `Shape` | Общ интерфейс с `calcArea()` |
| `Rectangle` | Независима имплементация |
| `Square` | Независима имплементация |

## Структура

```
problematic_snippet/   - Square extends Rectangle (нарушение)
solution/              - Shape интерфейс + независими класове
```
