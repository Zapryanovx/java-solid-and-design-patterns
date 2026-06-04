package design_patterns.creational.abstract_factory;

//Abstract factory with methods defined for each object type.
public interface ResourceFactory {

    Instance createInstance(Instance.Capacity capacity);

    Storage createStorage(int capacityInMib);
}
