package expression.exceptions;

import expression.Multiply;
import expression.Priority;

import java.util.function.BinaryOperator;

public class CheckedMultiply extends CheckedBinaryOperation {

    public CheckedMultiply(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Multiply.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return Multiply.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Multiply.SYMBOL;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Multiply. (Should've never happened).");
        }
        int a = operands[0], b = operands[1];
        if (a == 0 || b == 0) {
            return CheckResult.OKAY;
        }
        if (a == -1) {
            return b != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
        }
        if (a > 0 && b > 0 || a < 0 && b < 0) {
            if (a > 0 ? b <= Integer.MAX_VALUE / a : b >= Integer.MAX_VALUE / a) {
                return CheckResult.OKAY;
            }
        } else {
            if (a > 0 ? b >= Integer.MIN_VALUE / a : b <= Integer.MIN_VALUE / a) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }
}
