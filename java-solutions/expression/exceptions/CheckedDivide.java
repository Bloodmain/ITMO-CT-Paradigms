package expression.exceptions;

import expression.Divide;
import expression.Priority;

import java.util.function.BinaryOperator;

public class CheckedDivide extends CheckedBinaryOperation {

    public CheckedDivide(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Divide.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return Divide.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Divide.SYMBOL;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Divide. (Should've never happened).");
        }
        int a = operands[0], b = operands[1];
        if (b == -1) {
            return a != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
        }
        return b != 0 ? CheckResult.OKAY : CheckResult.DIVISION_BY_ZERO;
    }
}
