package expression;

import java.util.function.BinaryOperator;

public interface PriorityBinaryOperation extends PriorityExpression {
    BinaryOperator<Integer> getIntOperator();
    BinaryOperator<Double> getDoubleOperator();
}
