package design_patterns.creational.builder.with_director;

import java.io.Serializable;

public class UserWebDTO implements UserDTO {

    private String name;
    private String address;
    private String age;

    public UserWebDTO(String name, String address, String age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "name=" + name + "\n" +
               "age=" + age + "\n" +
               "address=" + address;
    }
}

