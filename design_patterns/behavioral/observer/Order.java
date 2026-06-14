package design_patterns.behavioral.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

//Concrete subject
public class Order {

    private final String id;

    private double shippingCost;

    //cost of items
    private double itemCost;

    private double discount;

    //no of items
    private int count;

    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(String id) {
        this.id = id;
    }

    public void attach(OrderObserver observer) {
        observers.add(observer);
    }

    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }

    public double getTotal() {
        return itemCost - discount + shippingCost;
    }

    public void addItem(double price) {
        itemCost += price;
        count++;

        // may be in some method
        observers.forEach(o -> o.updated(this));
    }

    public int getCount() {
        return count;
    }

    public double getItemCost() {
        return itemCost;
    }

    public void setShippingCost(double cost) {
        this.shippingCost = cost;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }


    @Override
    public String toString() {
        return "Order#" + id + "\nItem cost:" + itemCost + "\nNo. of items:" + count
                + "\nShipping cost:" + shippingCost + "\nDiscount:" + discount
                + "\nTotal:" + getTotal();
    }
}
