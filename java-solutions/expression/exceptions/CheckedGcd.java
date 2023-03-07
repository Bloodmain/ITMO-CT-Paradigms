package expression.exceptions;

import expression.Gcd;
import expression.Priority;

import java.util.function.BinaryOperator;

public class CheckedGcd extends CheckedBinaryOperation {

    public CheckedGcd(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Gcd.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return Gcd.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Gcd.SYMBOL;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Gcd. (Should've never happened).");
        }
        return CheckResult.OKAY;
    }
}
