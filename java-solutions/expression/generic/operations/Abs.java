package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Abs<T, W extends NumericType<T, W>> extends UnaryOperation<T, W> {
    public Abs(GenericExpression<T, W> operand) {
        super(operand);
    }

    @Override
    public W apply(W a) {
        return a.abs();
    }

    @Override
    public String getOperatorSymbol() {
        return "abs";
    }
}
