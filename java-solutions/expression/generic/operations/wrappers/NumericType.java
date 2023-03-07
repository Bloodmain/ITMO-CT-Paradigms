package expression.generic.operations.wrappers;

public interface NumericType<T, W extends NumericType<T, W>> {
    W add(NumericType<T, W> other);

    W subtract(NumericType<T, W> other);

    W multiply(NumericType<T, W> other);

    W divide(NumericType<T, W> other);
    W abs();
    W square();
    W mod(NumericType<T, W> other);

    W negate();

    W parseConst(String val);

    T getVal();
}
