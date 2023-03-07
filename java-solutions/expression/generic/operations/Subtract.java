package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Subtract<T, W extends NumericType<T, W>> extends BinaryOperation<T, W> {
    public Subtract(GenericExpression<T, W> leftOperand, GenericExpression<T, W> rightOperand) {
        super(leftOperand, rightOperand);
    }

    public W apply(W a, W b) {
        return a.subtract(b);
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }
}
