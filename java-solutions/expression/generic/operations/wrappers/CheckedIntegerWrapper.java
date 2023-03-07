package expression.generic.operations.wrappers;

import expression.generic.operations.checkers.ArithmeticChecker;
import expression.generic.operations.checkers.CheckResult;
import expression.exceptions.*;

public class CheckedIntegerWrapper implements NumericType<Integer, CheckedIntegerWrapper> {
    private final Integer val;

    public CheckedIntegerWrapper(Integer val) {
        this.val = val;
    }

    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public CheckedIntegerWrapper add(NumericType<Integer, CheckedIntegerWrapper> other) {
        CheckResult arithmeticCheck = ArithmeticChecker.checkAdd(val, other.getVal());
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(val + other.getVal());
        } else {
            throw new OverflowException("Overflow for " + val + " + " + other.getVal());
        }
    }

    @Override
    public CheckedIntegerWrapper subtract(NumericType<Integer, CheckedIntegerWrapper> other) {
        CheckResult arithmeticCheck = ArithmeticChecker.checkSubtract(val, other.getVal());
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(val - other.getVal());
        } else {
            throw new OverflowException("Overflow for " + val + " - " + other.getVal());
        }
    }

    @Override
    public CheckedIntegerWrapper multiply(NumericType<Integer, CheckedIntegerWrapper> other) {
        CheckResult arithmeticCheck = ArithmeticChecker.checkMultiply(val, other.getVal());
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(val * other.getVal());
        } else {
            throw new OverflowException("Overflow for " + val + " * " + other.getVal());
        }
    }

    @Override
    public CheckedIntegerWrapper divide(NumericType<Integer, CheckedIntegerWrapper> other) {
        CheckResult arithmeticCheck = ArithmeticChecker.checkDivide(val, other.getVal());
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(val / other.getVal());
        } else if (arithmeticCheck == CheckResult.DIVISION_BY_ZERO) {
            throw new DivisionByZeroException("Division by zero for " + val + " / " + other.getVal());
        } else {
            throw new OverflowException("Overflow for " + val + " / " + other.getVal());
        }
    }

    @Override
    public CheckedIntegerWrapper abs() {
        CheckResult arithmeticCheck = ArithmeticChecker.checkAbs(val);
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(Math.abs(val));
        } else {
            throw new OverflowException("Overflow for abs(" + val + ")");
        }
    }

    @Override
    public CheckedIntegerWrapper square() {
        return multiply(this);
    }

    @Override
    public CheckedIntegerWrapper mod(NumericType<Integer, CheckedIntegerWrapper> other) {
        CheckResult arithmeticCheck = ArithmeticChecker.checkMod(val, other.getVal());
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(val % other.getVal());
        } else if (arithmeticCheck == CheckResult.DIVISION_BY_ZERO) {
            throw new DivisionByZeroException("Division by zero for " + val + " % " + other.getVal());
        } else {
            throw new OverflowException("Overflow for " + val + " % " + other.getVal());
        }
    }

    @Override
    public CheckedIntegerWrapper negate() {
        CheckResult arithmeticCheck = ArithmeticChecker.checkNegate(val);
        if (arithmeticCheck == CheckResult.OKAY) {
            return new CheckedIntegerWrapper(-val);
        } else {
            throw new OverflowException("Overflow for -" + val);
        }
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public CheckedIntegerWrapper parseConst(String val) {
        return new CheckedIntegerWrapper(Integer.parseInt(val));
    }
}
