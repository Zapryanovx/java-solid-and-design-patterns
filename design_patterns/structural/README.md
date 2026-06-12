# Structural Patterns

Structural patterns описват как класове и обекти се **комбинират в по-големи структури**, без да нарушават гъвкавостта и поддръжката на системата. Фокусът е върху **композицията** - как обектите си взаимодействат и се "сглобяват", вместо как се създават (Creational) или как си общуват по поведение (Behavioral).

---

## Patterns

| Pattern | Идея | Детайли |
|---|---|---|
| [Adapter](adapter/README.md) | "Превежда" един интерфейс към друг, без да пипа нито един от двата | `EmployeeObjectAdapter`/`EmployeeClassAdapter` адаптират `Employee` към `Customer` |
| [Bridge](bridge/README.md) | Разделя **абстракцията** от **имплементацията**, за да се променят независимо | `FifoCollection` (какво) + `LinkedList` (как) |
| [Composite](composite/README.md) | Третира единичен обект и група от обекти **еднакво**, чрез общ интерфейс - за дървовидни структури | `File`/`Directory` |
| [Decorator](decorator/README.md) | "Увива" обект в друг обект със **същия интерфейс**, за да добавя поведение динамично | `TextMessage` → `HtmlEncodedMessage` → `Base64EncodedMessage` |
| [Facade](facade/README.md) | Опростен интерфейс пред сложна подсистема от класове | `EmailFacade` крие `Template`/`Stationary`/`Mailer`/`EmailBuilder` |
| [Flyweight](flyweight/README.md) | Споделя обекти с еднакво (intrinsic) състояние, вместо да създава нов за всяка употреба | `ErrorMessageFactory` кешира `SystemErrorMessage`/`UserBannedErrorMessage` |
| [Proxy](proxy/README.md) | Заместител (surrogate) пред друг обект, контролира достъпа до него (lazy loading, logging, права) | `virtual.ImageProxy` (static) и `dynamic.ImageInvocationHandler` (dynamic) |

Всеки пакет има собствен `README.md` с обяснение, участници, имплементация, примерна употреба и pitfalls.
