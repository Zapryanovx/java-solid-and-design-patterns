package shipment.price;

import shipment.Shipment;

public class PriorityBasedPriceStrategy implements PriceStrategy {
    private static final double BASE_VALUE = 2.00;
    private static final int SHIFTED_VALUE = 8;

    @Override
    public double calcDeliveryCost(Shipment shipment) {
        return BASE_VALUE * (SHIFTED_VALUE >> shipment.getType().getPriority());
    }
}
