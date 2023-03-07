package expression.exceptions;

import expression.UnaryOperation;

public abstract class CheckedUnaryOperation extends UnaryOperation implements CheckedExpression {
    public CheckedUnaryOperation(CheckedExpression operand) {
        super(operand);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a = operand.evaluate(x, y, z);
        switch (check(a)) {
            case NEGATIVE_LOG_POW_ARGUMENT -> throw new
                    NegativeLogPowException("Negative argument: " + getOperatorSymbol() + " " + a);
            case OVERFLOW -> throw new OverflowException("Overflow for " + getOperatorSymbol() + " " + a);
        }
        return getIntOperator().apply(a);
    }
}
