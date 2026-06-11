# Flyweight Pattern

Flyweight е **Structural** pattern, който намалява паметта чрез **споделяне** на обекти с еднакво ("intrinsic") състояние, вместо да създава нов обект за всяка употреба.

---

## Кога се ползва?

Когато трябва да създадеш **много обекти**, които имат **общи, повтарящи се данни** (intrinsic state), а само малка част от данните им е уникална за всяка конкретна употреба (extrinsic state).

В този пример - грешки в системата. Текстът/URL-ът на "Page Not Found" грешката е **общ** за всички такива грешки; уникален е само `errorCode`, който идва "отвън" при всяко извикване.

---

## Участници

| Роля | Клас |
|------|------|
| Flyweight (interface) | `ErrorMessage` |
| ConcreteFlyweight (shared) | `SystemErrorMessage` |
| UnsharedConcreteFlyweight | `UserBannedErrorMessage` |
| FlyweightFactory | `ErrorMessageFactory` |
| Client | `Client` |

---

## Имплементация

### `ErrorMessage` - общ интерфейс

```java
public interface ErrorMessage {
    //Get error message
    String getText(String code);
}
```

`code` е **extrinsic state** - не се пази никъде в обекта, идва като аргумент при всяко извикване на `getText`.

### `SystemErrorMessage` - shared flyweight

```java
public class SystemErrorMessage implements ErrorMessage {

    // some error message $errorCode
    private final String messageTemplate;

    // https:://somedomain.com/help?error=
    private final String helpUrlBase;

    public SystemErrorMessage(String messageTemplate, String helpUrlBase) {
        this.messageTemplate = messageTemplate;
        this.helpUrlBase = helpUrlBase;
    }

    @Override
    public String getText(String code) {
        return messageTemplate.replace("$errorCode", code)
                + helpUrlBase + code;
    }
}
```

- `messageTemplate`, `helpUrlBase` = **intrinsic** - общи за всички грешки от един тип, пазят се вътре в обекта, `final`.
- `code` = **extrinsic** - подава се при всяко извикване на `getText` и се комбинира с intrinsic данните.

### `UserBannedErrorMessage` - unshared flyweight

```java
public class UserBannedErrorMessage implements ErrorMessage {
    //All state is defined here
    private String caseId;
    private String remarks;
    private Duration banDuration;
    private String msg;

    public UserBannedErrorMessage(String caseId) {
        //Load case info from DB.
        this.caseId = caseId;
        remarks = "You violated terms of use.";
        banDuration = Duration.ofDays(2);
        msg = "You are BANNED. Sorry. \nMore information:\n";
        msg += caseId + "\n";
        msg += remarks + "\n";
        msg += "Banned For:" + banDuration.toHours() + " Hours";
    }

    //We ignore the extrinsic state argument
    @Override
    public String getText(String code) {
        return msg;
    }

    public String getCaseNo() {
        return caseId;
    }
}
```

Тук **всичко** е вградено в обекта при създаването (`caseId`, `remarks`, `banDuration`, готовото `msg`). Параметърът `code` от интерфейса се **игнорира** - тази инстанция не се споделя, всеки бан-случай получава своя собствена.

### `ErrorMessageFactory` - кешира и връща flyweight-и

```java
public class ErrorMessageFactory {

    public enum ErrorType { GenericSystemError, PageNotFoundError, ServerError }

    private static final ErrorMessageFactory FACTORY = new ErrorMessageFactory();

    public static ErrorMessageFactory getInstance() {
        return FACTORY;
    }

    private Map<ErrorType, SystemErrorMessage> errorMessages = new HashMap<>();

    private ErrorMessageFactory() {
        errorMessages.put(ErrorType.GenericSystemError,
                new SystemErrorMessage("A generic error of type $errorCode occured. Please refer to:\n", "http://google.com/q="));
        errorMessages.put(ErrorType.PageNotFoundError,
                new SystemErrorMessage("Page not found. An error of type $errorCode occured. Please refer to:\n", "http://google.com/q="));
    }

    public SystemErrorMessage getError(ErrorType errorType) {
        return errorMessages.get(errorType);
    }

    public UserBannedErrorMessage getUserBannedMessage(String caseId) {
        return new UserBannedErrorMessage(caseId);
    }
}
```

- `errorMessages` е кешът - попълва се **веднъж**, при създаването на единствената инстанция (Singleton).
- `getError(type)` **не създава** нов обект - връща готова инстанция от кеша → споделена между всички извиквания.
- `getUserBannedMessage(caseId)` винаги връща **нов** обект - unshared, затова не минава през кеша.

---

## Intrinsic vs Extrinsic в този пример

| | Intrinsic (вградено в обекта) | Extrinsic (подадено отвън) |
|--|--|--|
| `SystemErrorMessage` | `messageTemplate`, `helpUrlBase` | `code` (параметър на `getText`) |
| `UserBannedErrorMessage` | всичко (`caseId`, `remarks`, `banDuration`, `msg`) | няма - параметърът `code` се игнорира |

---

## Употреба

```java
SystemErrorMessage msg1 = ErrorMessageFactory.getInstance()
        .getError(ErrorMessageFactory.ErrorType.GenericSystemError);
System.out.println(msg1.getText("4056"));
// A generic error of type 4056 occured. Please refer to:
// http://google.com/q=4056

UserBannedErrorMessage msg2 = ErrorMessageFactory.getInstance()
        .getUserBannedMessage("1202");
System.out.println(msg2.getText("4056")); // "4056" се игнорира
// You are BANNED. Sorry.
// More information:
// 1202
// You violated terms of use.
// Banned For:48 Hours
```

`getError(GenericSystemError)` винаги връща **същия** обект - извикан 1000 пъти, factory-то не прави 1000 нови `SystemErrorMessage`.

---

## Singleton vs static factory

`ErrorMessageFactory` е Singleton (`private` конструктор + `getInstance()`), защото кешът `errorMessages` е **instance поле** - трябва да съществува само **една** инстанция на factory-то, за да има само **една** карта в паметта.

Алтернатива е изцяло `static` factory с `static` кеш (както `TemplateFactory` от Facade pattern) - постига същото без Singleton церемония. Двата подхода са еквивалентни за целите на Flyweight - изборът е стилистичен.

---

## Pitfalls

- `ErrorType.ServerError` е дефиниран в enum-а, но **никога не се добавя в `errorMessages`** → `getError(ErrorType.ServerError)` връща `null`, последващо `.getText(...)` хвърля `NullPointerException`.
- `UserBannedErrorMessage.getText(code)` игнорира `code` - ако някой очаква резултатът да зависи от него, ще се изненада.
- Кешът се пълни **eagerly** в конструктора - ако типовете грешки станат много, по-подходящо е lazy loading (`computeIfAbsent`).
- Flyweight-ите, които се споделят, трябва да са **immutable** (виж `final` полетата в `SystemErrorMessage`) - споделен mutable обект би причинил бъгове, защото промяна от едно място би се отразила навсякъде, където обектът е споделен.
