package client;

import logger.DeliveryLogger;
import shipment.Shipment;

import java.util.Objects;

public class Client implements DeliveryObserver {
    private final String name;
    private final String pin;
    private final String phone;

    public Client(String name, String pin, String phone) {
        this.name = name;
        this.pin = pin;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())  {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(pin, client.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pin);
    }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public void onDeliveryComplete(Shipment shipment) {
        DeliveryLogger.getInstance().log("Client " + name + " notified: shipment #" + shipment.getId() + " delivered");
    }
}
