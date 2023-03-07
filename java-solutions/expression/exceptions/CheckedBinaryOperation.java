package expression.exceptions;

import expression.BinaryOperation;

public abstract class CheckedBinaryOperation extends BinaryOperation implements CheckedExpression {
    public CheckedBinaryOperation(CheckedExpression leftOperand, CheckedExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    private void assertOkayArithmetic(int a1, int a2) {
        switch (check(a1, a2)) {
            case OVERFLOW -> {
                throw new OverflowException("Overflow for " + a1 + " " + getOperatorSymbol() + " " + a2);
            }
            case DIVISION_BY_ZERO -> {
                throw new DivisionByZeroException("Division by zero: " + a1 + " " + getOperatorSymbol() + " " + a2);
            }
        }
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int a1 = leftOperand.evaluate(x, y, z);
        int a2 = rightOperand.evaluate(x, y, z);
        assertOkayArithmetic(a1, a2);
        return getIntOperator().apply(a1, a2);
    }
}
