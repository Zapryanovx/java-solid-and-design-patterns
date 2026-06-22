package shipment;

public class Address {
    private final String country;
    private final String state;
    private final String city;
    private final String street;
    private final String zip;

    private Address(String country, String state, String city, String street, String zip) {
        this.country = country;
        this.state = state;
        this.city = city;
        this.street = street;
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return country + ", " +  state + ", " + city + ", " + street + ", " + zip;
    }

    public static class AddressBuilder {
        private String country;
        private String state;
        private String city;
        private String street;
        private String zip;

        public AddressBuilder withCountry(String country) {
            this.country = country;
            return this;
        }

        public AddressBuilder withState(String state) {
            this.state = state;
            return this;
        }

        public AddressBuilder withCity(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder withStreet(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder withZip(String zip) {
            this.zip = zip;
            return this;
        }

        public Address build() {
            return new Address(country, state, city, street, zip);
        }
    }
}
