package design_patterns.structural.proxy.dynamic;

import java.lang.reflect.Proxy;

import design_patterns.structural.proxy.Image;

//Factory to get image objects.
public class ImageFactory {
    //We'll provide proxy to caller instead of real object

    public static Image getImage() {
    	return (Image) Proxy.newProxyInstance(
                ImageFactory.class.getClassLoader(),
                new Class[] {Image.class},
                new ImageInvocationHandler()
        );
    }
}
