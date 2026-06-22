package company;

import logger.DeliveryLogger;
import shipment.Shipment;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class Company {
    private final String name;
    private final String phone;
    private final CopyOnWriteArrayList<Warehouse> warehouses;

    public Company(String name, String phone) {
        this.name = name;
        this.phone = phone;
        this.warehouses = new CopyOnWriteArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public CopyOnWriteArrayList<Warehouse> getWarehouses() {
        return warehouses;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", warehouses=" + warehouses +
                '}';
    }

    public void addWarehouse(Warehouse warehouse) {
        warehouses.add(warehouse);
    }

    public boolean addShipment(Shipment shipment) {
        DeliveryLogger logger = DeliveryLogger.getInstance();
        String targetCity = shipment.getAddress().getCity();

        for (Warehouse warehouse : warehouses) {
            if (warehouse.getAddress().getCity().equals(targetCity)) {
                if (warehouse.addShipment(shipment)) {
                    logger.log("Shipment #" + shipment.getId() + " added to warehouse in " + targetCity);
                    return true;
                }
                logger.log("Warehouse in " + targetCity + " is full, rerouting shipment #" + shipment.getId());
            }
        }

        for (Warehouse warehouse : warehouses) {
            if (warehouse.addShipment(shipment)) {
                logger.log("Shipment #" + shipment.getId() + " rerouted to warehouse in " + warehouse.getAddress().getCity());
                return true;
            }
        }

        logger.log("Shipment #" + shipment.getId() + " rejected - no available warehouse");
        return false;
    }
}
