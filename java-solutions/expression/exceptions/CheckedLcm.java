package expression.exceptions;

import expression.Gcd;
import expression.Lcm;
import expression.Priority;

import java.util.function.BinaryOperator;

public class CheckedLcm extends CheckedBinaryOperation {
    public CheckedLcm(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return Lcm.OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return Lcm.PRIORITY;
    }

    @Override
    public String getOperatorSymbol() {
        return Lcm.SYMBOL;
    }

    @Override
    public CheckResult check(int... operands) {
        if (operands.length != 2) {
            throw new AssertionError("Wrong operands number for Lcm. (Should've never happened).");
        }
        int a = operands[0], b = operands[1];
        int g = Gcd.gcd(a, b);
        return g == 0 ? CheckResult.OKAY : new CheckedMultiply(null, null).check(a / g, b);
    }
}
