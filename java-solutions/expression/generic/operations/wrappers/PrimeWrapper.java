package expression.generic.operations.wrappers;

import java.math.BigInteger;

public class PrimeWrapper implements NumericType<Integer, PrimeWrapper> {
    private final Integer val;
    private final static int MOD = 10079;
    
    private static int mathMod(int val) {
        return ((val % MOD) + MOD) % MOD;
    }

    public PrimeWrapper(int val) {
        this.val = mathMod(val);
    }

    @Override
    public PrimeWrapper add(NumericType<Integer, PrimeWrapper> other) {
        return new PrimeWrapper(val + other.getVal());
    }

    @Override
    public PrimeWrapper subtract(NumericType<Integer, PrimeWrapper> other) {
        return new PrimeWrapper(val - other.getVal());
    }

    @Override
    public PrimeWrapper multiply(NumericType<Integer, PrimeWrapper> other) {
        return new PrimeWrapper(val * other.getVal());
    }

    private int binPow(int a, int b) {
        if (b == 0) return 1;
        if (b % 2 == 1) return binPow(a, b - 1) * a % MOD;
        else {
            int t = binPow(a, b / 2);
            return t * t % MOD;
        }
    }

    private int inverseMod(int x) {
        return binPow(x, MOD - 2);
    }

    @Override
    public PrimeWrapper divide(NumericType<Integer, PrimeWrapper> other) {
        if (other.getVal() == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return new PrimeWrapper(val * inverseMod(other.getVal()));
    }

    @Override
    public PrimeWrapper abs() {
        return new PrimeWrapper(Math.abs(val));
    }

    @Override
    public PrimeWrapper square() {
        return multiply(this);
    }

    @Override
    public PrimeWrapper mod(NumericType<Integer, PrimeWrapper> other) {
        return new PrimeWrapper(val % other.getVal());
    }

    @Override
    public PrimeWrapper negate() {
        return new PrimeWrapper(-val);
    }

    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public PrimeWrapper parseConst(String val) {
        return new PrimeWrapper(Integer.parseInt(val));
    }
}
