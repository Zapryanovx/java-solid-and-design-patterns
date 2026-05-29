# Single Responsibility Principle (SRP)

> "A class should have only one reason to change."
> - Robert C. Martin

Всеки клас трябва да има **само една отговорност** - само една причина да бъде променен.

## Нарушение

`StudentService.addStudent()` прави 4 неща едновременно - валидация, бизнес логика, запис във файл и нотификация. Има **4 причини да се промени**.

## Решение

Всяка отговорност се извежда в отделен клас:

| Клас | Отговорност |
|------|-------------|
| `StudentValidator` | Валидира входните данни |
| `StudentRepository` | Пази in-memory колекцията |
| `StudentFileWriter` | Записва във файл |
| `NotificationService` | Изпраща нотификации |
| `StudentService` | Оркестрира всичко |

## Структура

```
problematic_snippet/   - StudentService с всичко в един клас
solution/              - разделено по отговорности
```
