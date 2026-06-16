package design_patterns.behavioral.template_method;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Order {

    private final String id;

    private final LocalDate date;

    private final Map<String, Double> items = new HashMap<>();

    private double total;

    public Order(String id) {
        this.id = id;
        date = LocalDate.now();
    }

    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Map<String, Double> getItems() {
        return items;
    }

    public void addItem(String name, double price) {
        items.put(name, price);
        total += price;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
