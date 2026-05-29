package solid.liskov_substitution.problematic_snippet;

public class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }

    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        super.setWeight(height);
    }

    @Override
    public void setWeight(double weight) {
        super.setHeight(weight);
        super.setWeight(weight);
    }
}