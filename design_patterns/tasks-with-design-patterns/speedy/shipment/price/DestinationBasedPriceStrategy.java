package shipment.price;

import shipment.Shipment;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DestinationBasedPriceStrategy implements PriceStrategy {
    private static final double DEFAULT_PRICE = 5.00;

    private final ConcurrentHashMap<String, Double> prices;

    public DestinationBasedPriceStrategy() {
        this.prices = new ConcurrentHashMap<>();
    }

    public void addPricing(String city, double price) {
        prices.put(city, price);
    }

    public Map<String, Double> getPrices() {
        return new HashMap<>(prices);
    }

    @Override
    public double calcDeliveryCost(Shipment shipment) {
        return prices.getOrDefault(shipment.getAddress().getCity(), DEFAULT_PRICE);
    }
}
