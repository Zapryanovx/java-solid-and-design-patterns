package shipment;

import client.Client;
import logger.DeliveryLogger;
import shipment.price.PriceStrategy;

public class ShipmentFactory {
    private final PriceStrategy strategy;

    public ShipmentFactory(PriceStrategy strategy) {
        this.strategy = strategy;
    }

    public Shipment create(Client client, Label label, long weight, ShipmentType type) {
        Shipment shipment = new Shipment(client, label, weight, type, strategy);
        DeliveryLogger.getInstance().log("Shipment was created:\n\t" + shipment);
        return shipment;
    }
}