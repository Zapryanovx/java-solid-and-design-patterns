package design_patterns.behavioral.null_object;

public class Client {

    public static void main(String[] args) {
        ComplexService service = new ComplexService("Simple report", new StorageService());
        service.generateReport();
    }
}
