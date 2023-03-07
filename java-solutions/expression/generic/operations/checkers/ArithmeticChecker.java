package expression.generic.operations.checkers;

public class ArithmeticChecker {
    public static CheckResult checkAdd(int a, int b) {
        if (b >= 0) {
            if ((a < 0 ? Integer.MAX_VALUE : Integer.MAX_VALUE - a) >= b) {
                return CheckResult.OKAY;
            }
        } else {
            if ((a > 0 ? Integer.MIN_VALUE : Integer.MIN_VALUE - a) <= b) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }

    public static CheckResult checkSubtract(int a, int b) {
        if (b >= 0) {
            if ((a >= 0 ? Integer.MAX_VALUE : a - Integer.MIN_VALUE) >= b) {
                return CheckResult.OKAY;
            }
        } else {
            if ((a < 0 ? Integer.MIN_VALUE : a - Integer.MAX_VALUE) <= b) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }

    public static CheckResult checkMultiply(int a, int b) {
        if (a == 0 || b == 0) {
            return CheckResult.OKAY;
        }
        if (a == -1) {
            return b != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
        }
        if (a > 0 && b > 0 || a < 0 && b < 0) {
            if (a > 0 ? b <= Integer.MAX_VALUE / a : b >= Integer.MAX_VALUE / a) {
                return CheckResult.OKAY;
            }
        } else {
            if (a > 0 ? b >= Integer.MIN_VALUE / a : b <= Integer.MIN_VALUE / a) {
                return CheckResult.OKAY;
            }
        }
        return CheckResult.OVERFLOW;
    }

    public static CheckResult checkDivide(int a, int b) {
        if (b == -1) {
            return a != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
        }
        return b != 0 ? CheckResult.OKAY : CheckResult.DIVISION_BY_ZERO;
    }

    public static CheckResult checkNegate(int a) {
        return a != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
    }

    public static CheckResult checkAbs(int a) {
        return a != Integer.MIN_VALUE ? CheckResult.OKAY : CheckResult.OVERFLOW;
    }

    public static CheckResult checkMod(int a, int b) {
        if (b == 0) {
            return CheckResult.DIVISION_BY_ZERO;
        }
        return CheckResult.OKAY;
    }
}
