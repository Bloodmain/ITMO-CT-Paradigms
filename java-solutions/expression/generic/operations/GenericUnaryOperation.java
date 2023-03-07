package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public interface GenericUnaryOperation<T, W extends NumericType<T, W>>
        extends GenericExpression<T, W> {
    W apply(W a);
}
