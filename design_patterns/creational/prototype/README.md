# Prototype Pattern

Prototype е **Creational** pattern, който създава нови обекти чрез **копиране на съществуващи** вместо от нулата.

---

## Кога се ползва?

Когато създаването на обект е скъпо (заявка към БД, сложна инициализация) и повечето инстанции имат **еднакъв стейт**:

```java
// Скъпо - всеки път от нулата
GameUnit u1 = new Swordsman(); // инициализация...
GameUnit u2 = new Swordsman(); // пак инициализация...

// Prototype - копираш вече готов обект
GameUnit u2 = u1.clone(); // бързо
```

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Prototype | `GameUnit` |
| Concrete Prototype | `Swordsman` |
| Non-clonable | `General` |
| Client | `Client` |

---

## Имплементация

### `GameUnit` - абстрактен prototype

```java
public abstract class GameUnit implements Cloneable {

    private Point3D position;
    protected String state = "idle";

    @Override
    public GameUnit clone() throws CloneNotSupportedException {
        GameUnit unit = (GameUnit) super.clone();
        unit.initialize(); // нулира стейта на клонинга
        return unit;
    }

    protected void initialize() {
        this.position = Point3D.ZERO;
        reset();
    }

    protected void reset() {
        this.state = "idle";
    }
}
```

### `Swordsman` - поддържа клониране

```java
public class Swordsman extends GameUnit {
    public void attack() {
        this.state = "attacking";
    }
}
```

### `General` - не поддържа клониране

```java
// Generals are unique - клонирането е забранено
public class General extends GameUnit {
    public void boostMorale() {
        this.state = "MoralBoost";
    }

    @Override
    public GameUnit clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Generals are unique");
    }
}
```

### `Client`

```java
Swordsman s1 = new Swordsman();
s1.move(new Point3D(-10, 0, 0), 20);
s1.attack();
System.out.println(s1); // Swordsman attacking @ (-10.0, 0.0, 0.0)

Swordsman s2 = (Swordsman) s1.clone();
System.out.println(s2); // Swordsman idle @ (0.0, 0.0, 0.0)
```

---

## Shallow vs Deep copy

| | Shallow copy | Deep copy |
|--|-------------|-----------|
| Примитиви (`float`, `int`) | ✅ копира стойността | ✅ копира стойността |
| Immutable (`String`) | ✅ безопасно | ✅ безопасно |
| Mutable обекти (`Address`) | ❌ копира само референцията | ✅ копира обекта |

`super.clone()` прави shallow copy — за mutable полета трябва ръчен deep copy.

---

## Prototype Registry

Когато клиентът няма директен достъп до прототипа — регистрираш го централно:

```java
public class GameUnitRegistry {
    private Map<String, GameUnit> prototypes = new HashMap<>();

    public void register(String name, GameUnit unit) {
        prototypes.put(name, unit);
    }

    public GameUnit get(String name) throws CloneNotSupportedException {
        return prototypes.get(name).clone();
    }
}

registry.register("swordsman", new Swordsman());
GameUnit s = registry.get("swordsman"); // клонира автоматично
```
