package expression.exceptions;

import expression.Priority;
import expression.Subtract;

import java.util.function.BinaryOperator;

public class CheckedSubtract extends CheckedBinaryOperation {
    public CheckedSubtract(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Subtract.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return Subtract.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Subtract.SYMBOL;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Subtract. (Should've never happened).");
        }
        int a = operands[0], b = operands[1];
        if (b >= 0) {
            if ((a >= 0 ? Integer.MAX_VALUE : a - Integer.MIN_VALUE) >= b) {
                return CheckResult.OKAY;
            }
        } else {
            if((a < 0 ? Integer.MIN_VALUE : a - Integer.MAX_VALUE) <= b) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }
}
