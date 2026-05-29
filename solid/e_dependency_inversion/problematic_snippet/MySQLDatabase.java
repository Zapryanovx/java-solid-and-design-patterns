package solid.e_dependency_inversion.problematic_snippet;

public class MySQLDatabase {
    public void save(String data) {
        System.out.println("Saving to MySQL: " + data);
    }
}
