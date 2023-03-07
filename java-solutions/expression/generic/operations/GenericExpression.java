package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public interface GenericExpression<T, W extends NumericType<T, W>> {
    W evaluate(W x, W y, W z);
    String getOperatorSymbol();
}
