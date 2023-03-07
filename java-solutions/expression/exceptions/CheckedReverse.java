package expression.exceptions;

import expression.Priority;
import expression.Reverse;

import java.util.function.UnaryOperator;

public class CheckedReverse extends CheckedUnaryOperation {
    public CheckedReverse(CheckedExpression operand) {
        super(operand);
    }

    @Override
    public Priority getPriority() {
        return Reverse.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Reverse.SYMBOL;
    }

    @Override
    public UnaryOperator<Integer> getIntOperator() {
        return Reverse.OPERATION_INT;
    }

    @Override
    public UnaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 1) {
            throw new AssertionError("Wrong operands number for Reverse. (Should've never happened).");
        }
        int a = operands[0];
        int res = 0;
        if (a < 0) {
            if (a == Integer.MIN_VALUE) {
                return CheckResult.OVERFLOW;
            }
            a = -a;
        }
        while (a > 0) {
            int digit = a % 10;
            a /= 10;
            if (res <= Integer.MAX_VALUE / 10 - digit) {
                res = res * 10 + digit;
            } else {
                return CheckResult.OVERFLOW;
            }
        }
        return CheckResult.OKAY;
    }
}
