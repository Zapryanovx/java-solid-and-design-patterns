package shipment.price;

import shipment.Shipment;

public class WeightBasedPriceStrategy implements PriceStrategy {
    private static final double BASE_COST = 3.00;
    private static final double COST_ON_PERCENTAGE_OF_WEIGHT = 0.001;

    @Override
    public double calcDeliveryCost(Shipment shipment) {
        return BASE_COST + shipment.getWeight() * COST_ON_PERCENTAGE_OF_WEIGHT;
    }
}
