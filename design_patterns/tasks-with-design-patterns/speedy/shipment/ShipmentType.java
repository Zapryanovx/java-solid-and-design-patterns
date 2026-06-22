package shipment;

public enum ShipmentType {
    EXPRESS(1, 1),
    FRAGILE(2, 3),
    STANDARD(3, 2);

    private final int priority;
    private final int deliveryDays;

    ShipmentType(int priority, int deliveryDays ) {
        this.priority = priority;
        this.deliveryDays = deliveryDays;
    }

    public int getPriority() {
        return priority;
    }

    public int getDeliveryDays() {
        return deliveryDays;
    }

    @Override
    public String toString() {
        return "ShipmentType{" +
                "priority=" + priority +
                ", deliveryDays=" + deliveryDays +
                '}';
    }
}
