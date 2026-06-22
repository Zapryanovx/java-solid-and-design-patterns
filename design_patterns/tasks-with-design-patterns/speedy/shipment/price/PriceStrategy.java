package shipment.price;

import shipment.Shipment;

public interface PriceStrategy {
    public double calcDeliveryCost(Shipment shipment);
}
