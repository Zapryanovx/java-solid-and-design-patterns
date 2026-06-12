package design_patterns.structural.proxy.dynamic;

import design_patterns.structural.proxy.Image;
import design_patterns.structural.proxy.Point2D;

public class Client {

    public static void main(String[] args) {
        Image img = ImageFactory.getImage();
        img.setLocation(new Point2D(-10, 0));
    }
}
