# Java SOLID & Design Patterns

Учебен проект на Java, покриващ SOLID принципите и 26 design pattern-а от класическата книга *Design Patterns: Elements of Reusable Object-Oriented Software* (Gang of Four). Всеки pattern е имплементиран с реален пример, придружен от README с обяснение, трасиран изход и pitfalls.

---

## Структура

```
├── solid/                  # SOLID принципи
└── design_patterns/
    ├── creational/         # Как се създават обекти
    ├── structural/         # Как се структурират класове и обекти
    └── behavioral/         # Как обектите комуникират помежду си
```

---

## SOLID принципи

| Принцип | Папка |
|---|---|
| **S** - Single Responsibility | [solid/a_single_responsibility](solid/a_single_responsibility/) |
| **O** - Open/Closed | [solid/b_open_closed](solid/b_open_closed/) |
| **L** - Liskov Substitution | [solid/c_liskov_substitution](solid/c_liskov_substitution/) |
| **I** - Interface Segregation | [solid/d_interface_segregation](solid/d_interface_segregation/) |
| **D** - Dependency Inversion | [solid/e_dependency_inversion](solid/e_dependency_inversion/) |

---

## Design Patterns

### Creational - [design_patterns/creational](design_patterns/creational/)
Управляват **създаването на обекти**, скривайки логиката на инстанциране.

| Pattern | Накратко |
|---|---|
| [Builder](design_patterns/creational/builder/) | Стъпково изграждане на сложни обекти |
| [Simple Factory](design_patterns/creational/simple_factory/) | Централизирано създаване по тип |
| [Factory Method](design_patterns/creational/factory_method/) | Подкласовете решават какъв обект да създадат |
| [Abstract Factory](design_patterns/creational/abstract_factory/) | Фамилии от свързани обекти без конкретни класове |
| [Prototype](design_patterns/creational/prototype/) | Клониране на съществуващи обекти |
| [Singleton](design_patterns/creational/singleton/) | Точно една инстанция в целия процес |
| [Object Pool](design_patterns/creational/object_pool/) | Повторна употреба на скъпи за създаване обекти |

### Structural - [design_patterns/structural](design_patterns/structural/)
Описват **как се комбинират класове и обекти** в по-големи структури.

| Pattern | Накратко |
|---|---|
| [Adapter](design_patterns/structural/adapter/) | Превежда несъвместими интерфейси |
| [Bridge](design_patterns/structural/bridge/) | Разделя абстракция от имплементация |
| [Composite](design_patterns/structural/composite/) | Дървовидни структури от еднотипни обекти |
| [Decorator](design_patterns/structural/decorator/) | Добавя поведение без наследяване |
| [Facade](design_patterns/structural/facade/) | Опростен интерфейс към сложна подсистема |
| [Flyweight](design_patterns/structural/flyweight/) | Споделяне на общото състояние между много обекти |
| [Proxy](design_patterns/structural/proxy/) | Заместник с контролиран достъп до реален обект |

### Behavioral - [design_patterns/behavioral](design_patterns/behavioral/)
Описват **комуникацията и разпределението на отговорности** между обекти.

| Pattern | Накратко |
|---|---|
| [Chain of Responsibility](design_patterns/behavioral/chain_of_responsibility/) | Верига от обработчици за заявка |
| [Command](design_patterns/behavioral/command/) | Заявка капсулирана като обект (undo, опашки) |
| [Interpreter](design_patterns/behavioral/interpreter/) | Граматика и интерпретация на изрази |
| [Iterator](design_patterns/behavioral/iterator/) | Последователен достъп без разкриване на структурата |
| [Mediator](design_patterns/behavioral/mediator/) | Централна комуникация между обекти |
| [Memento](design_patterns/behavioral/memento/) | Snapshot на състояние за undo |
| [Null Object](design_patterns/behavioral/null_object/) | No-op обект вместо null проверки |
| [Observer](design_patterns/behavioral/observer/) | Уведомяване при промяна на състояние |
| [State](design_patterns/behavioral/state/) | Поведение зависещо от вътрешно състояние |
| [Strategy](design_patterns/behavioral/strategy/) | Взаимозаменяеми алгоритми зад общ интерфейс |
| [Template Method](design_patterns/behavioral/template_method/) | Фиксиран скелет на алгоритъм с вариращи стъпки |
| [Visitor](design_patterns/behavioral/visitor/) | Нови операции върху йерархия без промяна на класовете |

---

## Технологии

- **Java** (JDK 17+)
- **IntelliJ IDEA**
- Без външни зависимости
