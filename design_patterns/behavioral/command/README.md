# Command Pattern

Command е **Behavioral** pattern, който капсулира "заявка за действие" (операция + параметри + получател) в самостоятелен обект. Подателят (Invoker) държи и изпълнява такива обекти, без да знае **какво** точно правят или **кой** реално ги обработва (Receiver).

---

## Кога се ползва?

Когато искаш да:
- подадеш "действие" другаде за изпълнение (опашка, друга нишка, по-късно във времето),
- Invoker-ът да работи с **един общ тип** (`Command`), независимо колко различни операции съществуват,
- логиката "как се изпълнява операцията" (Receiver) да остане напълно отделена от логиката "кога/в какъв ред се изпълнява" (Invoker).

В този пример - `MailTasksRunner` е опашка от задачи, изпълнявана от отделна работна нишка. Тя не знае нищо за `EWSService` или за "добавяне на член в списък" - просто вика `cmd.execute()` за всяко `Command`, което получи.

---

## Участници

| Роля | Клас |
|---|---|
| Command (interface) | `Command` |
| ConcreteCommand | `AddMemberCommand` |
| Receiver | `EWSService` |
| Invoker | `MailTasksRunner` |
| Client | `Client` |

---

## Имплементация

### `Command` - Command interface

```java
public interface Command {

    void execute();
}
```

Единственото, което Invoker-ът знае за командите - всяка от тях може да бъде "изпълнена". Колко и какви конкретни команди съществуват е напълно невидимо за `MailTasksRunner`.

### `EWSService` - Receiver

```java
public class EWSService {

    //Add a new member to mailing list
    public void addMember(String contact, String contactGroup) {
        //contact exchange
        System.out.println("Added " + contact + " to " + contactGroup);
    }

    //Remove member from mailing list
    public void removeMember(String contact, String contactGroup) {
        //contact exchange
        System.out.println("Removed " + contact + " from " + contactGroup);
    }
}
```

Тук живее **реалната логика** ("contact exchange" - в реален код би бил истински извик към Exchange Web Services). `contact` е email адрес, `contactGroup` е името на mailing list/distribution group, в който той се добавя или премахва.

### `AddMemberCommand` - Concrete Command

```java
public class AddMemberCommand implements Command {

    private final String emailAddress;
    private final String listName;
    private final EWSService receiver;

    public AddMemberCommand(
            String emailAddress, String listName, EWSService receiver) {
        this.emailAddress = emailAddress;
        this.listName = listName;
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.addMember(emailAddress, listName);
    }
}
```

`AddMemberCommand` "опакова" **всичко необходимо** за извикването: кой `receiver`, с какви параметри (`emailAddress`, `listName`) и каква операция (`addMember`). `execute()` просто го прави. `emailAddress`/`listName` са същите понятия като `contact`/`contactGroup` в `EWSService` - само преименувани за по-домейн-ориентиран Command-API.

### `MailTasksRunner` - Invoker

```java
public class MailTasksRunner implements Runnable {

    private final Thread runner;

    private final List<Command> pendingCommands;

    private volatile boolean stop;

    private static final MailTasksRunner RUNNER = new MailTasksRunner();

    public static final MailTasksRunner getInstance() {
        return RUNNER;
    }

    private MailTasksRunner() {
        pendingCommands = new LinkedList<>();
        runner = new Thread(this);
        runner.start();
    }

    @Override
    public void run() {
        while (true) {
            Command cmd = null;
            synchronized (pendingCommands) {
                if (pendingCommands.isEmpty()) {
                    try {
                        pendingCommands.wait();
                    } catch (InterruptedException e) {
                        System.out.println("Runner interrupted");
                        if (stop) {
                            System.out.println("Runner stopping");
                            return;
                        }
                    }
                }
                cmd = pendingCommands.isEmpty() ? null : pendingCommands.remove(0);
            }
            if (cmd == null) {
                return;
            }
            cmd.execute();
        }
    }

    public void addCommand(Command cmd) {
        synchronized (pendingCommands) {
            pendingCommands.add(cmd);
            pendingCommands.notifyAll();
        }
    }

    public void shutdown() {
        this.stop = true;
        this.runner.interrupt();
    }
}
```

`MailTasksRunner` е **singleton**, който при създаването си стартира собствена работна нишка (`runner`). `addCommand` слага `Command` в опашката и буди работната нишка с `notifyAll()`. Работната нишка взима по една команда от опашката и вика `cmd.execute()` - **извън** `synchronized` блока, за да не държи lock-а докато евентуално бавна операция (реален network call към EWS) се изпълнява.

`stop` е `volatile`, защото се пише от една нишка (тази, която вика `shutdown()`) и се чете от друга (`runner`), извън `synchronized` блок - вижда се веднага, без нужда от lock тук.

### `Client` (все още недовършен)

```java
public class Client {

    public static void main(String[] args) throws InterruptedException {
        EWSService service = new EWSService();

        Command c1 = new AddMemberCommand("a@a.com", "spam", service);
        MailTasksRunner ...
    }
}
```

---

## Употреба (илюстративно)

```java
EWSService service = new EWSService();

Command c1 = new AddMemberCommand("a@a.com", "spam", service);

MailTasksRunner.getInstance().addCommand(c1);

// ... по-късно, преди изход от приложението
MailTasksRunner.getInstance().shutdown();
```

Очакван изход (асинхронно, от работната нишка):
```
Added a@a.com to spam
```

`Client` създава `Command` и го подава на `MailTasksRunner` - не знае нищо за вътрешната опашка/нишка на Invoker-а, а `MailTasksRunner` не знае нищо за `EWSService`/`addMember`.

---

## Pitfalls

- **Non-daemon работна нишка** - `runner` се стартира в конструктора и никога не спира сама. Ако `Client` не извика `MailTasksRunner.getInstance().shutdown()`, JVM-ът няма да приключи - програмата ще "виси", въпреки че `main` е приключил.
- **Всеки `InterruptedException` спира нишката, не само `shutdown()`** - в `run()`, ако `wait()` бъде прекъснат по причина, различна от `shutdown()` (например spurious interrupt), `stop` е `false`, `if (stop)` не сработва, но изпълнението пада към `cmd = ... : null` → `cmd == null` → `return`. Работната нишка умира **без** `stop` да е `true`. Оттам нататък `addCommand()` продължава да трупа команди в опашката, но никой повече не я обработва - без грешка, без съобщение.
- **`emailAddress`/`listName` vs `contact`/`contactGroup`** - едни и същи понятия, различни имена между `AddMemberCommand` и `EWSService`. Полезно за по-четим Command-API, но лесно за объркване при дебъг.
- **`Command` е минимален интерфейс** - само `execute()`, без `undo()`. Ако по-късно потрябва Undo/Redo (виж Memento), `Command` ще трябва да се разшири - текущите `ConcreteCommand`-и нямат механизъм за отмяна.
- Коментарът `//Throw Away POC code DON'T USE in PROD` в `MailTasksRunner` не е случаен - горните проблеми са точно причината.
