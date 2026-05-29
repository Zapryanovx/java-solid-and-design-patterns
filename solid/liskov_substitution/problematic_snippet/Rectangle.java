package solid.liskov_substitution.problematic_snippet;

public class Rectangle {
    private double height;
    private double weight;

    public Rectangle() {
    }

    public Rectangle(double height, double weight) {
        this.height = height;
        this.weight = weight;
    }

    public double calcArea() {
        return weight * height;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
