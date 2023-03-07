package expression;

import java.util.Objects;
import java.util.function.BinaryOperator;

public abstract class BinaryOperation implements PriorityBinaryOperation {
    protected final PriorityExpression leftOperand;
    protected final PriorityExpression rightOperand;

    public BinaryOperation(final PriorityExpression leftOperand, final PriorityExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return getIntOperator().apply(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }
    @Override
    public String toString() {
        return "(" + leftOperand.toString() + ' ' + getOperatorSymbol() + ' ' + rightOperand.toString() + ")";
    }

    private String putInBracketsIfNeeded(String expr, boolean need) {
        if (need) {
            return "(" + expr + ")";
        }
        return expr;
    }

    @Override
    public String toMiniString() {
        return putInBracketsIfNeeded(leftOperand.toMiniString(),
                        getPriority().compareLeft(leftOperand.getPriority())) +
                ' ' + getOperatorSymbol() + ' ' +
                putInBracketsIfNeeded(rightOperand.toMiniString(),
                        getPriority().compareRight(rightOperand.getPriority()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, rightOperand, this.getClass());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation that) {
            return that.getClass() == this.getClass()
                    && leftOperand.equals(that.leftOperand)
                    && rightOperand.equals(that.rightOperand);
        }
        return false;
    }
}
