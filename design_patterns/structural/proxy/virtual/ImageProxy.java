package design_patterns.structural.proxy.virtual;

import design_patterns.structural.proxy.BitmapImage;
import design_patterns.structural.proxy.Image;
import design_patterns.structural.proxy.Point2D;

//Proxy class.
public class ImageProxy implements Image {

    private BitmapImage image;
    private String name;
    private Point2D location;

    public ImageProxy(String name) {
        this.name = name;
    }

    @Override
    public void setLocation(Point2D point2d) {
        if (image != null) {
            image.setLocation(point2d);
        } else {
            location = point2d;
        }
    }

    @Override
    public Point2D getLocation() {
        if (image != null) {
            return image.getLocation();
        }
        return location;
    }

    @Override
    public void render() {
        if (image == null) {
            image = new BitmapImage(name);
            if (location != null) {
                image.setLocation(location);
            }
        }
        image.render();
    }
}
