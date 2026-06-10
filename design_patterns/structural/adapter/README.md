# Adapter Pattern

Adapter е **Structural** pattern, който позволява на два несъвместими интерфейса да работят заедно. Адаптерът "превежда" единия интерфейс към другия - без да пипаш нито единия от двата.

---

## Кога се ползва?

Когато имаш **съществуващ клас**, но клиентът очаква **различен интерфейс** - и не можеш да промениш нито единия:

```
BusinessCardDesigner очаква Customer интерфейс
Employee има getFullName(), getJobTitle(), getOfficeLocation()
Customer иска getName(), getDesignation(), getAddress()
```

---

## Участници

| Роля | Клас |
|------|------|
| Target (очакван интерфейс) | `Customer` |
| Adaptee (съществуващ клас) | `Employee` |
| Object Adapter | `EmployeeObjectAdapter` |
| Class Adapter | `EmployeeClassAdapter` |
| Client | `BusinessCardDesigner` |

---

## Имплементация

### `Customer` - интерфейсът, който клиентът очаква

```java
public interface Customer {
    String getName();
    String getDesignation();
    String getAddress();
}
```

### `Employee` - съществуващият клас (не го пипаш)

```java
public class Employee {
    public String getFullName() { ... }
    public String getJobTitle() { ... }
    public String getOfficeLocation() { ... }
}
```

### `BusinessCardDesigner` - клиентът работи само с `Customer`

```java
public class BusinessCardDesigner {
    public String designCard(Customer customer) {
        return customer.getName() + "\n"
             + customer.getDesignation() + "\n"
             + customer.getAddress();
    }
}
```

---

## Два вида Adapter

### 1. Object Adapter - чрез композиция

Адаптерът **съдържа** Employee като поле и делегира към него:

```java
public class EmployeeObjectAdapter implements Customer {
    private final Employee adaptee; // композиция

    public EmployeeObjectAdapter(Employee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String getName() {
        return adaptee.getFullName(); // превежда
    }

    @Override
    public String getDesignation() {
        return adaptee.getJobTitle();
    }

    @Override
    public String getAddress() {
        return adaptee.getOfficeLocation();
    }
}
```

### 2. Class Adapter - чрез наследяване

Адаптерът **наследява** Employee и имплементира Customer:

```java
public class EmployeeClassAdapter extends Employee implements Customer {

    @Override
    public String getName() {
        return this.getFullName(); // наследено от Employee
    }

    @Override
    public String getDesignation() {
        return this.getJobTitle();
    }

    @Override
    public String getAddress() {
        return this.getOfficeLocation();
    }
}
```

---

## Употреба

```java
BusinessCardDesigner designer = new BusinessCardDesigner();

// Object Adapter
Employee employee = new Employee();
employee.setFullName("Elliot Alderson");
employee.setJobTitle("Security Engineer");
employee.setOfficeLocation("Allsafe Cybersecurity, New York");

EmployeeObjectAdapter adapter = new EmployeeObjectAdapter(employee);
System.out.println(designer.designCard(adapter));

// Class Adapter
EmployeeClassAdapter classAdapter = new EmployeeClassAdapter();
classAdapter.setFullName("Elliot Alderson");
classAdapter.setJobTitle("Security Engineer");
classAdapter.setOfficeLocation("Allsafe Cybersecurity, New York");

System.out.println(designer.designCard(classAdapter));
```

---

## Изход

```
Elliot Alderson
Security Engineer
Allsafe Cybersecurity, New York City, New York
```

---

## Object vs Class Adapter

| | Object Adapter | Class Adapter |
|--|----------------|---------------|
| Механизъм | Композиция | Наследяване |
| Гъвкавост | По-гъвкав - адаптира и наследници на Employee | Само конкретния клас |
| Two-way | Не | Да - може да се ползва и като Employee, и като Customer |
| Предпочитан | ✅ Да | Рядко |

---

## Pitfalls

- Не прекалявай с Adapter - ако контролираш класа, просто го промени
- Object Adapter е почти винаги по-добър избор от Class Adapter
- Adapter решава проблем с интерфейс - не с логика
