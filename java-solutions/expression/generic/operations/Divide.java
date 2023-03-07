package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Divide<T, W extends NumericType<T, W>> extends BinaryOperation<T, W> {
    public Divide(GenericExpression<T, W> leftOperand, GenericExpression<T, W> rightOperand) {
        super(leftOperand, rightOperand);
    }

    public W apply(W a, W b) {
        return a.divide(b);
    }

    @Override
    public String getOperatorSymbol() {
        return "/";
    }
}
