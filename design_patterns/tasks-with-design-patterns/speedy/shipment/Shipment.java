package shipment;

import client.Client;
import shipment.price.PriceStrategy;
import validator.Validator;

import java.util.concurrent.atomic.AtomicInteger;

public class Shipment {
    private static final AtomicInteger ID_COUNTER  = new AtomicInteger(0);
    private final int id;
    private final Client client;
    private final ShipmentType type;
    private final long weight;
    private final Label label;
    private final PriceStrategy strategy;
    private double price;

    public Shipment(Client client, Label label, long weight, ShipmentType type, PriceStrategy strategy) {
        this.client = client;
        this.label = Validator.requireNonNull(label, "label");
        this.weight = Validator.requirePositive(weight, "weight");
        this.type = Validator.requireNonNull(type, "type");
        this.strategy = Validator.requireNonNull(strategy, "strategy");
        id = ID_COUNTER.incrementAndGet();
        this.price = strategy.calcDeliveryCost(this);
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Label getLabel() {
        return label;
    }

    public Address getAddress() {
        return label.address();
    }

    public String getReceiver() {
        return label.receiver();
    }

    public String getBarcode() {
        return label.barcode();
    }

    public long getWeight() {
        return weight;
    }

    public ShipmentType getType() {
        return type;
    }

    public int getPriority() {
        return type.getPriority();
    }

    public int getDeliveryDays() {
        return type.getDeliveryDays();
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", client=" + client +
                ", type=" + type +
                ", weight=" + weight +
                ", label=" + label +
                ", price=" + price +
                '}';
    }
}
