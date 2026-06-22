package shipment;

import validator.Validator;

public record Label(Address address, String receiver, String barcode) {

    public Label {
        Validator.requireNonNull(address, "address");
        Validator.requireNonNull(receiver, "receiver");
        Validator.requireNonNull(barcode, "barcode");
    }

    @Override
    public String toString() {
        return "Label{" +
                "address=" + address +
                ", receiver='" + receiver + '\'' +
                ", barcode='" + barcode + '\'' +
                '}';
    }
}
