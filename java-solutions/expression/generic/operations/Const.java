package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Const<T, W extends NumericType<T, W>> implements GenericExpression<T, W> {
    private final String value;

    public Const(String value) {
        this.value = value;
    }

    @Override
    public W evaluate(W x, W y, W z) {
        return x.parseConst(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public String getOperatorSymbol() {
        return "";
    }
}
