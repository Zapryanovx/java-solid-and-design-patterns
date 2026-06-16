package design_patterns.behavioral.template_method;

import java.io.IOException;
import java.io.PrintWriter;

//Abstract base class defines the template method
public abstract class OrderPrinter {

    public final void printOrder(Order order, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(filename)) {
            out.println(start());
            out.println(formatOrderNumber(order));
            out.println(formatItems(order));
            out.println(formatTotal(order));
            out.println(end());
        }
    }

    protected abstract String start();

    protected abstract String formatOrderNumber(Order order);

    protected abstract String formatItems(Order order);

    protected abstract String formatTotal(Order order);

    protected abstract String end();
}
