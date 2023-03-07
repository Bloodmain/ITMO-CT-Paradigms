package expression;

import java.util.function.BinaryOperator;

public class Lcm extends BinaryOperation {
    public static final Priority PRIORITY = new Priority(1,
            OperationsGroups.GCD_LCM,
            OperationsProperties.HIGH_PRIORITY_IN_GROUP | OperationsProperties.REFLEXIVE);
    public static final String SYMBOL = "lcm";
    public static final BinaryOperator<Integer> OPERATION_INT = Lcm::lcm;

    public Lcm(PriorityExpression leftOperand, PriorityExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    public static int lcm(long a, long b) {
        int g = Gcd.gcd(a, b);
        return g == 0 ? 0 : (int) (a * b / g);
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
