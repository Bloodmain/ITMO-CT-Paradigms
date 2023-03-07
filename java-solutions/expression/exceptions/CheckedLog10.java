package expression.exceptions;

import expression.OperationsGroups;
import expression.OperationsProperties;
import expression.Priority;

import java.util.function.UnaryOperator;

public class CheckedLog10 extends CheckedUnaryOperation {
    public static final Priority PRIORITY = new Priority(0,
            OperationsGroups.UNARY,
            OperationsProperties.HIGH_PRIORITY_IN_GROUP | OperationsProperties.REFLEXIVE);
    public static final String SYMBOL = "log10";

    public CheckedLog10(CheckedExpression operand) {
        super(operand);
    }

    public static int log10(int a) {
        int result = 0;
        while (a >= 10) {
            a /= 10;
            result++;
        }
        return result;
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
        return CheckedLog10::log10;
    }

    @Override
    public UnaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 1) {
            throw new AssertionError("Wrong operands number for Log. (Should've never happened).");
        }
        int a = operands[0];
        if (a <= 0) {
            return CheckResult.NEGATIVE_LOG_POW_ARGUMENT;
        }
        return CheckResult.OKAY;
    }
}
