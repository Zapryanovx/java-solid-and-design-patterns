package design_patterns.structural.facade.email;

public class StationaryFactory {

    public static Stationary createStationary() {
        return new HalloweenStationary();
    }
}
