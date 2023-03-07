package expression.exceptions;

import expression.OperationsGroups;
import expression.OperationsProperties;
import expression.Priority;

import java.util.function.UnaryOperator;

public class CheckedPow10 extends CheckedUnaryOperation {
    public static final Priority PRIORITY = new Priority(0,
            OperationsGroups.UNARY,
            OperationsProperties.HIGH_PRIORITY_IN_GROUP | OperationsProperties.REFLEXIVE);
    public static final String SYMBOL = "pow10";

    public CheckedPow10(CheckedExpression operand) {
        super(operand);
    }

    public static int pow10(int a) {
        if (a == 0) {
            return 1;
        }
        if (a % 2 != 0) {
            return pow10(a - 1) * 10;
        } else {
            int b = pow10(a / 2);
            return b * b;
        }
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    @Override
    public UnaryOperator<Integer> getIntOperator() {
        return CheckedPow10::pow10;
    }

    @Override
    public UnaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 1) {
            throw new AssertionError("Wrong operands number for Pow. (Should've never happened).");
        }
        int a = operands[0];
        if (a < 0) {
            return CheckResult.NEGATIVE_LOG_POW_ARGUMENT;
        }
        return a > 9 ? CheckResult.OVERFLOW : CheckResult.OKAY;
    }
}
