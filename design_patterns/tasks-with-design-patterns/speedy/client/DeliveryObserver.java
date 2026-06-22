package client;

import shipment.Shipment;

public interface DeliveryObserver {
    void onDeliveryComplete(Shipment shipment);
}