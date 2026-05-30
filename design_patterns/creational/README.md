# Creational Design Patterns

Creational patterns решават **как се създават обекти**. Целта е да се скрие логиката на създаване и да се намали зависимостта от конкретни класове.

---

## Видове

| Pattern | Цел |
|---------|-----|
| **Builder** | Строи сложни обекти стъпка по стъпка |
| **Simple Factory** | Централизира създаването на обекти в един клас |
| **Factory Method** | Делегира създаването на обект на наследници |
| **Prototype** | Създава нови обекти чрез копиране на съществуващи |
| **Abstract Factory** | Създава фамилии от свързани обекти |
| **Singleton** | Гарантира само една инстанция на клас |
| **Object Pool** | Преизползва вече създадени обекти вместо да създава нови |

---

## Структура

```
creational/
├── builder/          - Builder pattern
├── simple_factory/   - Simple Factory pattern
├── factory_method/   - Factory Method pattern
├── prototype/        - Prototype pattern
├── abstract_factory/ - Abstract Factory pattern
├── singleton/        - Singleton pattern
└── object_pool/      - Object Pool pattern
```
