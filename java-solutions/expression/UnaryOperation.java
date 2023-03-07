package expression;

import java.util.Objects;

public abstract class UnaryOperation implements PriorityUnaryOperation {
    protected final PriorityExpression operand;

    public UnaryOperation(PriorityExpression operand) {
        this.operand = operand;
    }
    @Override
    public int evaluate(int x, int y, int z) {
        return getIntOperator().apply(operand.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return getOperatorSymbol() + "(" + operand.toString() + ")";
    }

    @Override
    public String toMiniString() {
        if (getPriority().compareRight(operand.getPriority())) {
            return getOperatorSymbol() + "(" + operand.toMiniString() + ")";
        }
        return getOperatorSymbol() + " " + operand.toMiniString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation that) {
            return this.getClass() == that.getClass() &&
                    this.operand.equals(that.operand);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand, this.getClass());
    }
}
