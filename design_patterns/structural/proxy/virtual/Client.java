package design_patterns.structural.proxy.virtual;

import design_patterns.structural.proxy.Image;
import design_patterns.structural.proxy.Point2D;

public class Client {

    public static void main(String[] args) {
        Image img = ImageFactory.getImage("A1.bmp");

        img.setLocation(new Point2D(10, 10));
        System.out.println("Image location: " + img.getLocation());
        System.out.println("rendering image now...");
        img.render();
    }
}
