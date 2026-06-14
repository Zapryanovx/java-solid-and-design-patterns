package design_patterns.behavioral.observer;

public class Client {

    public static void main(String[] args) {
        Order order = new Order("100");

        OrderObserver price = new PriceObserver();
        order.attach(price);

        order.addItem(50);
        System.out.println(order);

        System.out.println("-----------------");

        order.addItem(179);
        System.out.println(order);
    }
}
