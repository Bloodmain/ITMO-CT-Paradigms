package expression.generic;

@FunctionalInterface
public interface SupplierException<T, E extends Exception> {
    T get() throws E;
}
