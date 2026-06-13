# Interpreter Pattern

Interpreter е **Behavioral** pattern, който представя правилата на един "малък език" (граматика) като дърво от обекти, и определя как да "интерпретираш" (оцениш) конкретно изречение от този език, обхождайки това дърво.

В този пример "езикът" са **правила за достъп** - текстови изрази като `"NOT FINANCE_ADMIN"` или `"FINANCE_USER AND ADMIN"`, съставени от permission-имена и оператори `AND`/`OR`/`NOT`.

---

## Кога се ползва?

Когато имаш **прост, добре дефиниран език** (тук - булеви изрази над permission-и), който:
- се парсва **веднъж** в дърво от обекти (AST),
- се оценява **многократно**, срещу различни входове (тук - различни `User`-и),
- всяко правило в граматиката (terminal/nonterminal) има собствена логика за оценка, изразена като отделен клас с метод `interpret(...)`.

---

## Участници

| Роля | Клас |
|---|---|
| AbstractExpression | `PermissionExpression` |
| TerminalExpression | `Permission` |
| NonterminalExpression | `AndExpression`, `OrExpression`, `NotExpression` |
| Context | `User` |
| Sentence (суров вход за парсване) | `Report` |
| Builder/Parser (изгражда AST) | `ExpressionBuilder` |
| Client | `Client` |

---

## Имплементация

### `PermissionExpression` - AbstractExpression

```java
public interface PermissionExpression {

    boolean interpret(User user);
}
```

Единен договор за всички възли в дървото - всеки знае как да отговори "удовлетворява ли този `User` мен (и под-изразите ми)?".

### `Permission` - TerminalExpression

```java
public class Permission implements PermissionExpression {

    private final String permission;

    public Permission(String permission) {
        this.permission = permission.toUpperCase();
    }

    @Override
    public boolean interpret(User user) {
        return user.getPermissions().contains(permission);
    }

    @Override
    public String toString() {
        return permission;
    }
}
```

Листо в дървото - няма под-изрази. Просто проверява дали `user` притежава точно този permission.

### `AndExpression` / `OrExpression` / `NotExpression` - NonterminalExpression

```java
public class AndExpression implements PermissionExpression {

    private final PermissionExpression left;
    private final PermissionExpression right;

    public AndExpression(
            PermissionExpression left, PermissionExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(User user) {
        return left.interpret(user) && right.interpret(user);
    }

    @Override
    public String toString() {
        return left + " AND " + right;
    }
}
```

```java
public class OrExpression implements PermissionExpression {

    private PermissionExpression left;
    private PermissionExpression right;

    public OrExpression(PermissionExpression left, PermissionExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(User user) {
        return left.interpret(user) || right.interpret(user);
    }

    @Override
    public String toString() {
        return left + " OR " + right;
    }
}
```

```java
public class NotExpression implements PermissionExpression {

    private PermissionExpression expression;

    public NotExpression(PermissionExpression expression) {
        this.expression = expression;
    }

    @Override
    public boolean interpret(User user) {
        return !expression.interpret(user);
    }

    @Override
    public String toString() {
        return " NOT " + expression;
    }
}
```

Всеки от тях има под-израз(и) (`left`/`right` или `expression`) и **рекурсивно** вика `interpret(user)` върху тях, комбинирайки резултата с `&&`, `||` или `!`.

### `User` - Context

```java
public class User {

    private List<String> permissions;

    private String username;

    public User(String username, String... permissions) {
        this.username = username;
        this.permissions = new ArrayList<>();
        Stream.of(permissions).forEach(e -> this.permissions.add(e.toLowerCase()));
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public String getUsername() {
        return username;
    }

}
```

Носи данните, срещу които се оценява изразът - тук permission-ите на конкретния потребител. AST-възлите не знаят откъде идва `User`, просто им се подава при `interpret(user)`.

### `Report` - Sentence (вход)

```java
public class Report {

    private String name;

    //"NOT ADMIN", "FINANCE_USER AND ADMIN"
    private String permission;

    public Report(String name, String permissions) {
        this.name = name;
        this.permission = permissions;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

}
```

`permission` е суров текст на "изречение" от граматиката - точно това, което `ExpressionBuilder` ще парсне в AST.

### `ExpressionBuilder` - Parser / AST builder

```java
public class ExpressionBuilder {

    private Stack<PermissionExpression> permissions = new Stack<>();

    private Stack<String> operators = new Stack<>();

    public PermissionExpression build(Report report) {
        parse(report.getPermission());
        buildExpressions();
        if (permissions.size() > 1 || !operators.isEmpty()) {
            System.out.println("ERROR!");
        }
        return permissions.pop();
    }

    private void parse(String permission) {
        StringTokenizer tokenizer = new StringTokenizer(permission.toLowerCase());
        while (tokenizer.hasMoreTokens()) {
            String token;
            switch ((token = tokenizer.nextToken())) {
            case "and":
                operators.push("and");
                break;
            case "not":
                operators.push("not");
                break;
            case "or":
                operators.push("or");
                break;
            default:
                permissions.push(new Permission(token));
                break;
            }
        }
    }

    private void buildExpressions() {
        while (!operators.isEmpty()) {
            String operator = operators.pop();
            PermissionExpression perm1;
            PermissionExpression perm2;
            PermissionExpression exp;
            switch (operator) {
            case "not":
                perm1 = permissions.pop();
                exp = new NotExpression(perm1);
                break;
            case "and":
                perm1 = permissions.pop();
                perm2 = permissions.pop();
                exp = new AndExpression(perm1, perm2);
                break;
            case "or":
                perm1 = permissions.pop();
                perm2 = permissions.pop();
                exp = new OrExpression(perm1, perm2);
                break;
            default:
                throw new IllegalArgumentException("Unknown operator:" + operator);
            }
            permissions.push(exp);
        }
    }
}
```

- `parse` токенизира суровия текст (`"not finance_admin"` → `"not"`, `"finance_admin"`) и пълни две стекове - `operators` (операторите по реда им) и `permissions` (`Permission`-и за всеки токен, който не е оператор).
- `buildExpressions` минава през `operators` отгоре-надолу, "сглобява" съответния `NonterminalExpression` от върха/върховете на `permissions` и го бута обратно в `permissions`.
- Накрая в `permissions` остава **точно един** елемент - коренът на AST-то, който се връща от `build()`.

### `Client`

```java
public class Client {

    public static void main(String[] args) {
        Report report = new Report(
                "Cashdlow report",
                "NOT FINANCE_ADMIN"
        );

        ExpressionBuilder builder = new ExpressionBuilder();
        PermissionExpression exp = builder.build(report);
        System.out.println(exp);

        User user = new User(
                "Dave",
                "USER",
                "ADMIN"
        );

        System.out.println("User access report: " + exp.interpret(user));
    }
}
```

---

## Употреба

Изход:
```
 NOT FINANCE_ADMIN
User access report: true
```

`Client` парсва правилото `"NOT FINANCE_ADMIN"` веднъж в AST (`NotExpression(Permission(FINANCE_ADMIN))`), после го оценява срещу `Dave` (с permissions `USER`, `ADMIN`). Тъй като Dave няма `FINANCE_ADMIN`, `Permission(FINANCE_ADMIN).interpret(Dave)` връща `false`, а `NotExpression` обръща резултата → `true`.

---

## Pitfalls

- **Case mismatch между `User` и `Permission`** - `User` пази permission-ите си с `.toLowerCase()`, а `Permission` ги пази с `.toUpperCase()`. `user.getPermissions().contains(permission)` сравнява lowercase списък с uppercase стринг → **никога не съвпада**. Резултат: `Permission(X).interpret(user)` винаги е `false`, независимо дали `user` реално има `X`. С правило `"NOT ADMIN"` и `Dave`, който **има** `ADMIN`, това дава `true` вместо коректното `false`. С `"NOT FINANCE_ADMIN"` (горният пример) бъгът не личи, защото Dave наистина няма `FINANCE_ADMIN` - и грешен, и коректен резултат дават `true`.
- **Без operator precedence/скоби** - `ExpressionBuilder` е линеен two-stack парсър без поддръжка на `(...)` групиране. Работи само за плоски изрази от вида `"A and B"`, `"not A"`, `"A or B"` - не за `"(A and B) or C"`.
- **Реда на операндите се "обръща"** - в `buildExpressions`, `perm1`/`perm2` се взимат чрез `pop()` от стека, така че `AndExpression(perm1, perm2)` получава операндите в обратен ред спрямо реда им във входния текст. За `&&`/`||` това е семантично безразлично (комутативни), но `toString()` показва обърнат ред спрямо оригиналния текст.
- **`NotExpression.toString()` връща `" NOT " + expression`** - с водещ интервал, затова изходът е `" NOT FINANCE_ADMIN"`, не `"NOT FINANCE_ADMIN"`.
- **`build()` не хвърля при `"ERROR!"`** - ако `permissions.size() > 1 || !operators.isEmpty()`, само се принтира `"ERROR!"`, но кодът продължава към `permissions.pop()` - при невалиден вход това може да хвърли `EmptyStackException` вместо ясна грешка.
