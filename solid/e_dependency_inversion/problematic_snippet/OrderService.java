package solid.e_dependency_inversion.problematic_snippet;

public class OrderService {
    private final MySQLDatabase database = new MySQLDatabase();

    public void placeOrder(String order) {
        database.save(order);
    }
}
