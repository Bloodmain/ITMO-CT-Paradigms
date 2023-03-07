package expression.generic.operations;

import expression.generic.operations.wrappers.NumericType;

public class Variable<T, W extends NumericType<T, W>> implements GenericExpression<T, W> {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    public W evaluate(W x, W y, W z) {
        return (name.equals("x") ? x : (name.equals("y") ? y : z));
    }

    @Override
    public String getOperatorSymbol() {
        return "";
    }

    @Override
    public String toString() {
        return name;
    }
}
