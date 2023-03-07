package expression;

import java.util.function.BinaryOperator;

public class Add extends BinaryOperation {
    public static final Priority PRIORITY = new Priority(0,
            OperationsGroups.ADDICTIVE,
            OperationsProperties.REFLEXIVE | OperationsProperties.ASSOCIATIVE);
    public static final String SYMBOL = "+";
    public static final BinaryOperator<Integer> OPERATION_INT = Integer::sum;
    public static final BinaryOperator<Double> OPERATION_DOUBLE = Double::sum;

    public Add(PriorityExpression leftOperand, PriorityExpression rightOperand) {
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
