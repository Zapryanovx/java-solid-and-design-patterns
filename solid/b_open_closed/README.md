# Open/Closed Principle (OCP)

> "Software entities should be open for extension, but closed for modification."
> - Robert C. Martin

Класовете трябва да са **отворени за разширяване**, но **затворени за модификация**. Ново изискване = нов клас, не редакция на стар.

## Нарушение

`Employee.calculateSalary()` съдържа if/else верига по тип позиция. Всяка нова позиция (`INTERN`, `TEAM_LEAD`) изисква **модификация** на съществуващия клас.

## Решение

`Employee` става абстрактен клас с абстрактен метод `calculateSalary()`. Всяка позиция е отделен клас, който го имплементира. Нова позиция = **само нов клас**.

| Клас | Отговорност |
|------|-------------|
| `Employee` | Абстрактен базов клас |
| `Developer` | Изчислява заплата за developer |
| `Manager` | Изчислява заплата за manager |

## Структура

```
problematic_snippet/   - Employee с if/else верига
solution/              - абстрактен Employee + наследници
```
