package solid.e_dependency_inversion.solution;

public class OrderService {
    private final Database database;

    OrderService(Database database) {
        this.database = database;
    }

    public void placeOrder(String order) {
        database.save(order);
    }
}
