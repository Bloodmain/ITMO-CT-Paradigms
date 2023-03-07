package expression.generic.operations.wrappers;

public class IntegerWrapper implements NumericType<Integer, IntegerWrapper> {
    private final Integer val;

    public IntegerWrapper(Integer val) {
        this.val = val;
    }

    @Override
    public IntegerWrapper add(NumericType<Integer, IntegerWrapper> other) {
        return new IntegerWrapper(val + other.getVal());
    }

    @Override
    public IntegerWrapper subtract(NumericType<Integer, IntegerWrapper> other) {
        return new IntegerWrapper(val - other.getVal());
    }

    @Override
    public IntegerWrapper multiply(NumericType<Integer, IntegerWrapper> other) {
        return new IntegerWrapper(val * other.getVal());
    }

    @Override
    public IntegerWrapper divide(NumericType<Integer, IntegerWrapper> other) {
        return new IntegerWrapper(val / other.getVal());
    }

    @Override
    public IntegerWrapper abs() {
        return new IntegerWrapper(Math.abs(val));
    }

    @Override
    public IntegerWrapper square() {
        return multiply(this);
    }

    @Override
    public IntegerWrapper mod(NumericType<Integer, IntegerWrapper> other) {
        return new IntegerWrapper(val % other.getVal());
    }

    @Override
    public IntegerWrapper negate() {
        return new IntegerWrapper(-val);
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
    public IntegerWrapper parseConst(String val) {
        return new IntegerWrapper(Integer.parseInt(val));
    }
}
