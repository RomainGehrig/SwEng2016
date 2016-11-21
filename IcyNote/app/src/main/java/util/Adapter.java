package util;

/**
 * Created by Julien on 20/11/2016.
 */
public interface Adapter<A, B> {
    B from(A b);
    A to(B a);
}