# Null Object Pattern

Null Object е **Behavioral** pattern, при който вместо `null` се подава обект, имплементиращ същия интерфейс/базов клас, но **не правещ нищо**. Елиминира `null` проверките в кода на клиента.

В този пример `ComplexService` зависи от `StorageService`. При тестване или при режим "без запис" вместо да подаваш `null` (и да добавяш `if (storage != null)` проверки навсякъде), подаваш `NullStorageService` - той приема извикванията, но не извършва реална операция.

---

## Кога се ползва?

Когато:
- зависимост е опционална и искаш да избегнеш `null` проверки в клиента,
- искаш "тихо" поведение по подразбиране (не прави нищо, върни празна стойност) без условна логика.

Примери от Java стандартната библиотека: `Collections.emptyList()`, `Optional.empty()`.

---

## Участници

| Роля | Клас |
|---|---|
| RealObject | `StorageService` |
| NullObject | `NullStorageService` |
| Client (ползва зависимостта) | `ComplexService` |
| Client (конфигурира) | `Client` |
| Supporting | `Report` |

---

## Имплементация

### `StorageService` - RealObject

```java
public class StorageService {

    public void save(Report report) {
        System.out.println("Writing report out");
        try (PrintWriter writer = new PrintWriter(report.getName() + ".txt")) {
            writer.println(report.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

Реалната имплементация - записва репорта в `.txt` файл.

### `NullStorageService` - NullObject

```java
public class NullStorageService extends StorageService {

    @Override
    public void save(Report report) {
        System.out.println("Null object save method. Doing nothing.");
    }
}
```

Разширява `StorageService` и override-ва `save()` да не прави нищо. `ComplexService` не знае и не го интересува с коя имплементация работи.

### `ComplexService` - Client

```java
public class ComplexService {

    private final StorageService storage;

    private final String reportName;

    public ComplexService(StorageService storage) {
        this.storage = storage;
        reportName = "A Complex Report";
    }

    public ComplexService(String reportName, StorageService storage) {
        this.storage = storage;
        this.reportName = reportName;
    }

    public void generateReport() {
        System.out.println("Starting a complex report build!");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done with report..");
        storage.save(new Report(reportName)); // без null проверка
    }
}
```

`ComplexService` извиква `storage.save(...)` директно, без `if (storage != null)`. Дали ще се запише файл или не зависи изцяло от това коя имплементация е подадена в конструктора.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        ComplexService service = new ComplexService("Simple report", new NullStorageService());
        service.generateReport();
    }
}
```

`Client` решава поведението чрез избора на имплементация. Смяна на `new NullStorageService()` с `new StorageService()` активира реалния запис - без промяна в `ComplexService`.

---

## Употреба

Трасиране на `Client.main` с `NullStorageService`:

1. `new ComplexService("Simple report", new NullStorageService())` - инжектира Null Object.
2. `generateReport()` стартира, изчаква 3 секунди, после извиква `storage.save(report)`.
3. `NullStorageService.save()` се извиква - принтира съобщение, не записва файл.

```
Starting a complex report build!
Done with report..
Null object save method. Doing nothing.
```

При `new StorageService()` изходът би бил:

```
Starting a complex report build!
Done with report..
Writing report out
```

И би се създал файл `Simple report.txt`.

---

## Pitfalls

- **`StorageService` е конкретен клас, не абстрактен** - ако се добави нов метод (напр. `delete()`), `NullStorageService` ще наследи реалната имплементация мълчаливо, вместо да я игнорира. С абстрактен базов клас или интерфейс компилаторът би наложил имплементацията.
- **`println` в `NullStorageService.save()`** - в реална употреба Null Object не трябва да печата нищо; той трябва да е напълно "тих". Съобщението е само за илюстрация.
- **`Thread.sleep(3000)`** в `generateReport()` блокира текущия thread за 3 секунди - в продукционен код генерирането на репорт би се изпълнявало асинхронно.
