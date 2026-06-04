package design_patterns.creational.abstract_factory.gcp;

import design_patterns.creational.abstract_factory.Instance;
import design_patterns.creational.abstract_factory.ResourceFactory;
import design_patterns.creational.abstract_factory.Storage;

//Factory implementation for Google Cloud Platform resources
public class GoogleResourceFactory implements ResourceFactory {

    @Override
    public Instance createInstance(Instance.Capacity capacity) {
        return new GoogleComputeEngineInstance(capacity);
    }

    @Override
    public Storage createStorage(int capacityInMib) {
        return new GoogleCloudStorage(capacityInMib);
    }
}
