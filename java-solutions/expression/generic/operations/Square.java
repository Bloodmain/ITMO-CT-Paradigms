package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Square<T, W extends NumericType<T, W>> extends UnaryOperation<T, W> {
    public Square(GenericExpression<T, W> operand) {
        super(operand);
    }

    @Override
    public W apply(W a) {
        return a.square();
    }

    @Override
    public String getOperatorSymbol() {
        return "square";
    }
}
