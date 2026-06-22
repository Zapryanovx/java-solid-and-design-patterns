package company;

import logger.DeliveryLogger;
import shipment.Address;
import shipment.Shipment;
import validator.Validator;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Warehouse {
    private final Address address;
    private final int capacity;
    private final PriorityQueue<Shipment> shipments =
            new PriorityQueue<>(new ByPriorityComparator());

    public Warehouse(Address address, int capacity) {
        this.address = address;
        this.capacity = capacity;
    }

    public Address getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public PriorityQueue<Shipment> getShipments() {
        return new PriorityQueue<>(shipments);
    }

    public synchronized boolean addShipment(Shipment shipment) {
        Validator.requireNonNull(shipment, "shipment");

        if (capacity == shipments.size()) {
            return false;
        }
        shipments.add(shipment);
        DeliveryLogger.getInstance()
                .log("Shipment" + " #" + shipment.getId() + " was added to warehouse at " + address);
        return true;
    }

    public synchronized ConcurrentLinkedQueue<Shipment> getShipments(String route, long weightLimit) {
        ConcurrentLinkedQueue<Shipment> queue = new ConcurrentLinkedQueue<>();
        Iterator<Shipment> iterator = shipments.iterator();
        long currWeight = 0;

        while (iterator.hasNext()) {
            Shipment shipment = iterator.next();
            if (shipment.getAddress().getCity().equals(route)
                && currWeight + shipment.getWeight() <= weightLimit) {
                queue.add(shipment);
                currWeight += shipment.getWeight();
                iterator.remove();
            }
        }
        return queue;
    }
}
