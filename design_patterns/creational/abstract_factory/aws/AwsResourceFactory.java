package design_patterns.creational.abstract_factory.aws;

import design_patterns.creational.abstract_factory.Instance;
import design_patterns.creational.abstract_factory.ResourceFactory;
import design_patterns.creational.abstract_factory.Storage;

//Factory implementation for Amazon Web Services resources
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
