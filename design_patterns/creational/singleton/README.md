# Singleton Pattern

Singleton е **Creational** GoF pattern, който гарантира, че даден клас има **само една инстанция** в цялото приложение.

---

## Кога се ползва?

Когато имаш споделен ресурс, който трябва да е **един за цялото приложение**:
- Database connection pool
- Logger
- Configuration manager
- Cache

---

## Имплементации

### 1. Eager - създава се при зареждане на класа

```java
public class EagerRegistry {
    private EagerRegistry() { }

    private static final EagerRegistry INSTANCE = new EagerRegistry();

    public static EagerRegistry getInstance() {
        return INSTANCE;
    }
}
```

✅ Прост код  
✅ Thread-safe (JVM гарантира)  
❌ Създава се винаги - дори ако не се ползва

---

### 2. Lazy с DCL (Double Checked Locking)

```java
public class LazyRegistryWithDCL {
    private LazyRegistryWithDCL() { }

    private static volatile LazyRegistryWithDCL INSTANCE;

    public static LazyRegistryWithDCL getInstance() {
        if (INSTANCE == null) {                      // 1. Бърза проверка без lock
            synchronized (LazyRegistryWithDCL.class) { // 2. Lock само ако е null
                if (INSTANCE == null) {              // 3. Проверка след lock
                    INSTANCE = new LazyRegistryWithDCL();
                }
            }
        }
        return INSTANCE;
    }
}
```

✅ Lazy - създава се само при нужда  
✅ Thread-safe  
⚠️ `volatile` е задължителен - предотвратява пренареждане на инструкции  
❌ Сложен код

---

### 3. Lazy с IODH (Initialization On Demand Holder)

```java
public class LazyRegistryIODH {
    private LazyRegistryIODH() { }

    private static class RegistryHolder {
        static LazyRegistryIODH INSTANCE = new LazyRegistryIODH();
    }

    public static LazyRegistryIODH getInstance() {
        return RegistryHolder.INSTANCE;
    }
}
```

✅ Lazy - `RegistryHolder` се зарежда само при първи `getInstance()`  
✅ Thread-safe (JVM гарантира при зареждане на клас)  
✅ Без `volatile` и `synchronized`  
✅ Чист код

---

### 4. Enum (препоръчван от Joshua Bloch)

```java
public enum RegistryEnum {
    INSTANCE;

    public void someMethod() { }
}
```

✅ Минимален код  
✅ Thread-safe  
✅ Serialization вградена  
❌ Не е lazy

---

## Сравнение

| | Eager | DCL | IODH | Enum |
|--|-------|-----|------|------|
| Lazy | ❌ | ✅ | ✅ | ❌ |
| Thread-safe | ✅ | ✅ | ✅ | ✅ |
| Serialization | ❌ | ❌ | ❌ | ✅ |
| Сложност | Минимална | Висока | Ниска | Минимална |
