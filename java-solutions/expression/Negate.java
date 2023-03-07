package expression;

import java.util.function.UnaryOperator;

public class Negate extends UnaryOperation {
    public static final Priority PRIORITY = new Priority(0,
            OperationsGroups.UNARY,
            OperationsProperties.REFLEXIVE | OperationsProperties.ASSOCIATIVE);
    public static final String SYMBOL = "-";
    public static final UnaryOperator<Integer> OPERATION_INT = a -> -a;
    public static final UnaryOperator<Double> OPERATION_DOUBLE = a -> -a;

    public Negate(PriorityExpression operand) {
       super(operand);
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
        return OPERATION_DOUBLE;
    }
}
