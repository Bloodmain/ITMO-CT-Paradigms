package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Mod<T, W extends NumericType<T, W>> extends BinaryOperation<T, W> {
    public Mod(GenericExpression<T, W> leftOperand, GenericExpression<T, W> rightOperand) {
        super(leftOperand, rightOperand);
    }

    public W apply(W a, W b) {
        return a.mod(b);
    }

    @Override
    public String getOperatorSymbol() {
        return "mod";
    }
}
