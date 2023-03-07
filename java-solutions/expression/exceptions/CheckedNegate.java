package expression.exceptions;

import expression.Negate;
import expression.Priority;

import java.util.function.UnaryOperator;

public class CheckedNegate extends CheckedUnaryOperation {
    public CheckedNegate(CheckedExpression operand) {
        super(operand);
    }

    @Override
    public Priority getPriority() {
        return Negate.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Negate.SYMBOL;
    }

    @Override
    public UnaryOperator<Integer> getIntOperator() {
        return Negate.OPERATION_INT;
    }

    @Override
    public UnaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 1) {
            throw new AssertionError("Wrong operands number for Negate. (Should've never happened).");
        }
        return operands[0] != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
    }
}
