package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public abstract class UnaryOperation<T, W extends NumericType<T, W>>
        implements GenericUnaryOperation<T, W> {
    protected final GenericExpression<T, W> operand;

    public UnaryOperation(GenericExpression<T, W> operand) {
        this.operand = operand;
    }

    @Override
    public W evaluate(W x, W y, W z) {
        return apply(operand.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getOperatorSymbol() + "(" + operand.toString() + ")";
    }
}
