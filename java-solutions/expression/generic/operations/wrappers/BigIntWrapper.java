package expression.generic.operations.wrappers;

import java.math.BigInteger;
import java.util.function.Function;

public class BigIntWrapper implements NumericType<BigInteger, BigIntWrapper> {
    private final BigInteger val;

    public BigIntWrapper(BigInteger val) {
        this.val = val;
    }

    public BigIntWrapper(Integer val) {
        this.val = BigInteger.valueOf(val);
    }

    @Override
    public BigInteger getVal() {
        return val;
    }

    @Override
    public BigIntWrapper add(NumericType<BigInteger, BigIntWrapper> other) {
        return new BigIntWrapper(val.add(other.getVal()));
    }

    @Override
    public BigIntWrapper subtract(NumericType<BigInteger, BigIntWrapper> other) {
        return new BigIntWrapper(val.subtract(other.getVal()));
    }

    @Override
    public BigIntWrapper multiply(NumericType<BigInteger, BigIntWrapper> other) {
        return new BigIntWrapper(val.multiply(other.getVal()));
    }

    @Override
    public BigIntWrapper divide(NumericType<BigInteger, BigIntWrapper> other) {
        return new BigIntWrapper(val.divide(other.getVal()));
    }

    @Override
    public BigIntWrapper abs() {
        return new BigIntWrapper(val.abs());
    }

    @Override
    public BigIntWrapper square() {
        return multiply(this);
    }

    @Override
    public BigIntWrapper mod(NumericType<BigInteger, BigIntWrapper> other) {
        return new BigIntWrapper(val.mod(other.getVal()));
    }

    @Override
    public BigIntWrapper negate() {
        return new BigIntWrapper(val.negate());
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public BigIntWrapper parseConst(String val) {
        return new BigIntWrapper(BigInteger.valueOf(Integer.parseInt(val)));
    }
}
