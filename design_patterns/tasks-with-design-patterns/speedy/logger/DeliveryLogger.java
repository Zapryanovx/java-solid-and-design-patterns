package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class DeliveryLogger {
    private static final String LOG_FILE = "deliveries.log";
    private static final DeliveryLogger INSTANCE = new DeliveryLogger();

    public static synchronized DeliveryLogger getInstance() {
        return INSTANCE;
    }

    public synchronized void log(String message) {
        String formatted = "[" + LocalDateTime.now() + "] " + message;
        System.out.println(formatted);

        try (var pw = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            pw.println(formatted);
        } catch (IOException e) {
            throw new RuntimeException("Couldnt log to file", e);
        }
    }
}