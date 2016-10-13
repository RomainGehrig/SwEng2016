package util;

import java.util.NoSuchElementException;

/**
 * Handcrafted version of java 8's optional for compatibility purpose.
 *
 * @param <T> type of the contained object
 * @author Julien Harbulot
 * @version 1.0
 */
public abstract class Optional<T> {

    private static final Optional<?> sEMPTY = new Empty<>();

    public static <A> Optional<A> of(A contained) {
        return new Container<>(contained);
    }

    public static <A> Optional<A> empty() {
        @SuppressWarnings("unchecked")
        Optional<A> opt = (Optional<A>) sEMPTY;
        return opt;
    }

    public abstract boolean isPresent();

    public abstract T get();

    private static class Empty<T> extends Optional<T> {
        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public T get() {
            throw new NoSuchElementException("Optional is empty");
        }
    }

    private static class Container<T> extends Optional<T> {
        private T mContained = null;

        public Container(T contained) {
            if (contained != null) {
                mContained = contained;
            } else {
                throw new java.lang.NullPointerException("Optional may not contain null argument");
            }
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public T get() {
            return mContained;
        }
    }

}