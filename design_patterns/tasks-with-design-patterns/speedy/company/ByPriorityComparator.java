package company;

import shipment.Shipment;

import java.util.Comparator;

public class ByPriorityComparator implements Comparator<Shipment> {
    @Override
    public int compare(Shipment o1, Shipment o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
