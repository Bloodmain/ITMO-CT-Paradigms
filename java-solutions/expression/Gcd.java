package expression;

import java.util.function.BinaryOperator;

public class Gcd extends BinaryOperation {
    public static final Priority PRIORITY = new Priority(0,
            OperationsGroups.GCD_LCM,
            OperationsProperties.HIGH_PRIORITY_IN_GROUP | OperationsProperties.REFLEXIVE);
    public static final String SYMBOL = "gcd";
    public static final BinaryOperator<Integer> OPERATION_INT = Gcd::gcd;

    public Gcd(PriorityExpression leftOperand, PriorityExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    public static int gcd(long a, long b) {
        return (int) Math.abs(b == 0 ? a : gcd(b, a % b));
    }

    @Override
    public String getOperatorSymbol() {
        return SYMBOL;
    }

    @Override
    public BinaryOperator<Integer> getIntOperator() {
        return OPERATION_INT;
    }

    @Override
    public BinaryOperator<Double> getDoubleOperator() {
        return null;
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
