package expression;

import expression.exceptions.CheckResult;
import expression.exceptions.CheckedExpression;

public class Const implements PriorityExpression, CheckedExpression {
    private final static Priority priority = new Priority(0,
            OperationsGroups.VARIABLES,
            0);
    private final Number value;

    public Const(final int value) {
        this.value = value;
    }

    public Const(final double value) {
        this.value = value;
    }

    @Override
    public int evaluate(final int x, final int y, final int z) {
        return value.intValue();
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String getOperatorSymbol() {
        return "";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Const that) {
            return value.equals(that.value);
        }
        return false;
    }

    @Override
    public CheckResult check(int... operands) {
        return CheckResult.OKAY;
    }
}
