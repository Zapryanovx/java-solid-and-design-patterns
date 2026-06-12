package design_patterns.structural.proxy.dynamic;

import design_patterns.structural.proxy.BitmapImage;
import design_patterns.structural.proxy.Image;
import design_patterns.structural.proxy.Point2D;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

//Implement invocation handler. Your "proxy" code goes here.
public class ImageInvocationHandler implements InvocationHandler {

    private BitmapImage image;
    private String name;
    private Point2D location;

    public ImageInvocationHandler(String name) {
        this.name = name;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method setLocationMethod = Image.class.getMethod("setLocation", new Class[]{Point2D.class});
        Method getLocationMethod = Image.class.getMethod("getLocation");
        Method renderMethod = Image.class.getMethod("render");

        if (setLocationMethod.equals(method)) {
            Point2D point2d = (Point2D) args[0];
            System.out.println("From InvocationHandler: " + point2d);
            if (image != null) {
                image.setLocation(point2d);
            } else {
                location = point2d;
            }
            return null;
        }

        if (getLocationMethod.equals(method)) {
            if (image != null) {
                return image.getLocation();
            }
            return location;
        }

        if (renderMethod.equals(method)) {
            if (image == null) {
                image = new BitmapImage(name);
                if (location != null) {
                    image.setLocation(location);
                }
            }
            image.render();
            return null;
        }

        return null;
    }
}
