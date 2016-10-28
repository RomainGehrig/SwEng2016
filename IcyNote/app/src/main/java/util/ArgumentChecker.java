package util;

public final class ArgumentChecker {
    // Disable instantiation
    private ArgumentChecker() {}

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object shouldn't be null");
        } else {
            return obj;
        }
    }
}
