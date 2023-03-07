package expression.exceptions;

import expression.Add;
import expression.Priority;

import java.util.function.BinaryOperator;

public class CheckedAdd extends CheckedBinaryOperation {

    public CheckedAdd(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    public Priority getPriority() {
        return Add.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Add.SYMBOL;
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Add.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Add. (Should've never happened).");
        }
        int a = operands[0], b = operands[1];
        if (b >= 0) {
            if ((a < 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE - a) >= b) {
                return CheckResult.OKAY;
            }
        } else {
            if ((a > 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - a) <= b) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }
}
