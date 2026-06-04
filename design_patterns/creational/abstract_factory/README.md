# Abstract Factory Pattern

Abstract Factory е **Creational** pattern, който създава **фамилии от свързани обекти** без да зависи от конкретните им класове.

---

## Кога се ползва?

Когато системата трябва да работи с различни **фамилии продукти** (AWS, GCP) и искаш лесно да смениш цялата фамилия наведнъж:

```java
// Сменяш само фабриката - всичко останало е същото
Client aws = new Client(new AwsResourceFactory());
Client gcp = new Client(new GoogleResourceFactory());
```

---

## Участници

| Роля | Клас |
|------|------|
| Abstract Factory | `ResourceFactory` |
| Concrete Factory | `AwsResourceFactory`, `GoogleResourceFactory` |
| Abstract Product | `Instance`, `Storage` |
| Concrete Products (AWS) | `Ec2Instance`, `S3Storage` |
| Concrete Products (GCP) | `GoogleComputeEngineInstance`, `GoogleCloudStorage` |
| Client | `Client` |

---

## Имплементация

### `ResourceFactory` - абстрактна фабрика

```java
public interface ResourceFactory {
    Instance createInstance(Instance.Capacity capacity);
    Storage createStorage(int capacityInMib);
}
```

### `AwsResourceFactory` - конкретна фабрика за AWS

```java
public class AwsResourceFactory implements ResourceFactory {
    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new Ec2Instance(capacity);
    }

    @Override
    public Storage createStorage(int capacityInMib) {
        return new S3Storage(capacityInMib);
    }
}
```

### `Client` - ползва само абстракциите

```java
public class Client {
    private ResourceFactory resourceFactory;

    public Client(ResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public Instance createServer(Instance.Capacity capacity, int storageMb) {
        Instance instance = resourceFactory.createInstance(capacity);
        Storage storage = resourceFactory.createStorage(storageMb);
        instance.attachStorage(storage);
        return instance;
    }
}
```

### Използване

```java
Client aws = new Client(new AwsResourceFactory());
Instance i1 = aws.createServer(Instance.Capacity.micro, 20480);
i1.start();
i1.stop();

Client gcp = new Client(new GoogleResourceFactory());
Instance i2 = gcp.createServer(Instance.Capacity.micro, 20480);
i2.start();
i2.stop();
```

---

## Разлика от Factory Method

| | Factory Method | Abstract Factory |
|--|---------------|-----------------|
| Създава | Един продукт | Фамилия от продукти |
| Разширяване | Нов наследник | Нова фабрика |
| Пример | `MessageCreator` → `Message` | `ResourceFactory` → `Instance` + `Storage` |
