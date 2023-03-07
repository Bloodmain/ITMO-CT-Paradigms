package expression;

import java.util.function.UnaryOperator;

public class Reverse extends UnaryOperation {
    public static final Priority PRIORITY = new Priority(1,
            OperationsGroups.UNARY,
            OperationsProperties.REFLEXIVE | OperationsProperties.ASSOCIATIVE);
    public static final String SYMBOL = "reverse";
    public static final UnaryOperator<Integer> OPERATION_INT = Reverse::reverse;

    public Reverse(PriorityExpression operand) {
        super(operand);
    }

    public static int reverse(long a) {
        boolean negative = false;
        int res = 0;
        if (a < 0) {
            a = -a;
            negative = true;
        }
        while (a > 0) {
            int digit = (int) (a % 10);
            a /= 10;
            res = res * 10 + digit;
        }
        return negative ? -res : res;
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
        return OPERATION_INT;
    }

    @Override
    public UnaryOperator<Double> getDoubleOperator() {
        return null;
    }
}
