package expression;

import java.util.function.UnaryOperator;

public interface PriorityUnaryOperation extends PriorityExpression {
    UnaryOperator<Integer> getIntOperator();
    UnaryOperator<Double> getDoubleOperator();
}
