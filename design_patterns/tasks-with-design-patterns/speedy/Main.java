import client.Client;
import company.Company;
import company.Courier;
import company.Warehouse;
import logger.DeliveryLogger;
import shipment.Address;
import shipment.Label;
import shipment.Shipment;
import shipment.ShipmentType;
import shipment.price.PriceStrategy;
import shipment.price.WeightBasedPriceStrategy;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

void main() throws InterruptedException {
    DeliveryLogger logger = DeliveryLogger.getInstance();

    // Setup
    Company company = new Company("SpeedyCourier", "0888123456");

    Address sofiAddr = new Address.AddressBuilder().withCity("Sofia").build();
    Address plovdivAddr = new Address.AddressBuilder().withCity("Plovdiv").build();

    company.addWarehouse(new Warehouse(sofiAddr, 50));
    company.addWarehouse(new Warehouse(plovdivAddr, 50));

    Courier courier1 = new Courier(10000, "Sofia", company.getWarehouses().get(0));
    Courier courier2 = new Courier(10000, "Plovdiv", company.getWarehouses().get(1));

    // Shipment generator - creates 100 shipments periodically
    ScheduledExecutorService generator = Executors.newScheduledThreadPool(1);
    List<String> cities = List.of("Sofia", "Plovdiv");
    PriceStrategy strategy = new WeightBasedPriceStrategy();
    Random random = new Random();
    int[] count = {0};

    generator.scheduleAtFixedRate(() -> {
        if (count[0] >= 100) {
            generator.shutdown();
            return;
        }
        Client client = new Client("Client" + count[0], "PIN" + count[0], "0888" + count[0]);
        String city = cities.get(random.nextInt(cities.size()));
        Address addr = new Address.AddressBuilder().withCity(city).build();
        Label label = new Label(addr, "Receiver" + count[0], "BAR" + count[0]);
        ShipmentType type = ShipmentType.values()[random.nextInt(3)];
        Shipment shipment = new Shipment(client, label, 500 + random.nextInt(2000), type, strategy);

        logger.log("Shipment #" + shipment.getId() + " created");
        company.addShipment(shipment);
        count[0]++;
    }, 0, 200, TimeUnit.MILLISECONDS);

    // Couriers run in parallel
    ExecutorService couriers = Executors.newFixedThreadPool(2);
    couriers.submit(courier1);
    couriers.submit(courier2);

    // Wait for generator to finish, then wait for couriers
    generator.awaitTermination(30, TimeUnit.SECONDS);
    couriers.shutdown();
    couriers.awaitTermination(60, TimeUnit.SECONDS);

    logger.log("Simulation complete");
}