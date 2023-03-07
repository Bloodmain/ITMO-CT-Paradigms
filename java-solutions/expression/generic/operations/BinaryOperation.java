package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public abstract class BinaryOperation<T, W extends NumericType<T, W>> implements GenericBinaryOperation<T, W> {
    protected final GenericExpression<T, W> leftOperand;
    protected final GenericExpression<T, W> rightOperand;

    public BinaryOperation(
            final GenericExpression<T, W> leftOperand,
            final GenericExpression<T, W> rightOperand
    ) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public W evaluate(W x, W y, W z) {
        return apply(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + ' ' + getOperatorSymbol() + ' ' + rightOperand.toString() + ")";
    }
}
