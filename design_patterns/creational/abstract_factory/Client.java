package design_patterns.creational.abstract_factory;

import design_patterns.creational.abstract_factory.aws.AwsResourceFactory;
import design_patterns.creational.abstract_factory.gcp.GoogleResourceFactory;

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

    public static void main(String[] args) {
        Client aws = new Client(new AwsResourceFactory());
        Instance i1 = aws.createServer(Instance.Capacity.micro, 20480);
        i1.start();
        i1.stop();

        System.out.println("***********************");

        Client gcp = new Client(new GoogleResourceFactory());
        Instance i2 = gcp.createServer(Instance.Capacity.micro, 20480);
        i2.start();
        i2.stop();
    }
}
