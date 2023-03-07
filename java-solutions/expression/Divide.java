package expression;

import java.util.function.BinaryOperator;

public class Divide extends BinaryOperation {
    public static final Priority PRIORITY = new Priority(1,
            OperationsGroups.MULTIPLICATIVE,
            OperationsProperties.HIGH_PRIORITY_IN_GROUP);
    public static final String SYMBOL = "/";
    public static final BinaryOperator<Integer> OPERATION_INT = (a, b) -> a / b;
    public static final BinaryOperator<Double> OPERATION_DOUBLE = (a, b) -> a / b;

    public Divide(PriorityExpression leftOperand, PriorityExpression rightOperand) {
        super(leftOperand, rightOperand);
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
        return OPERATION_DOUBLE;
    }

    @Override
    public Priority getPriority() {
        return PRIORITY;
    }
}
