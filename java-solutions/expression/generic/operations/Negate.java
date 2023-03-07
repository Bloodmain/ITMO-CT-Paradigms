package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Negate<T, W extends NumericType<T, W>> extends UnaryOperation<T, W> {
    public Negate(GenericExpression<T, W> operand) {
        super(operand);
    }

    @Override
    public W apply(W a) {
        return a.negate();
    }

    @Override
    public String getOperatorSymbol() {
        return "-";
    }
}
