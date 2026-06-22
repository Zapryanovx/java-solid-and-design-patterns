package company;

import logger.DeliveryLogger;
import shipment.Shipment;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Courier implements Runnable {
    private final long weightLimit;
    private final String route;
    private final ConcurrentLinkedQueue<Shipment> shipments;

    public Courier(long weightLimit, String route, Warehouse warehouse) {
        this.weightLimit = weightLimit;
        this.route = route;
        this.shipments = warehouse.getShipments(route, weightLimit);
    }

    public long getWeightLimit() {
        return weightLimit;
    }

    public String getRoute() {
        return route;
    }

    public List<Shipment> getShipments() {
        return shipments.stream().toList();
    }

    @Override
    public void run() {
        DeliveryLogger logger = DeliveryLogger.getInstance();
        Shipment shipment;
        while ((shipment = shipments.poll()) != null) {
            try {
                logger.log("Delivery started: shipment #" + shipment.getId() + " on route " + route);
                Thread.sleep(shipment.getDeliveryDays() * 1000L);
                logger.log("Delivery completed: shipment #" + shipment.getId() + " on route " + route);
                shipment.getClient().onDeliveryComplete(shipment);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
