package solid.liskov_substitution.problematic_snippet;

public class Square extends Rectangle {
    public Square(double side) {
        super(side, side);
    }

    @Override
    public void setHeight(double height) {
        super.setHeight(height);
        super.setWidth(height);
    }

    @Override
    public void setWidth(double weight) {
        super.setHeight(weight);
        super.setWidth(weight);
    }
}