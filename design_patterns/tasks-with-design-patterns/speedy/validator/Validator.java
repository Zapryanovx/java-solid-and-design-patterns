package validator;

public class Validator {

    public static <T> T requireNonNull(T obj, String type) {
        if (obj == null) {
            throw new IllegalArgumentException(type + " should not be null");
        }
        return obj;
    }

    public static long requirePositive(long value, String type) {
        if (value <= 0) {
            throw new IllegalArgumentException(type + " should not be negative");
        }
        return value;
    }

}
