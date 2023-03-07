package expression.generic.operations.wrappers;

public class DoubleWrapper implements NumericType<Double, DoubleWrapper> {
    private final Double val;

    public DoubleWrapper(Double val) {
        this.val = val;
    }

    public DoubleWrapper(Integer val) {
        this.val = (double) val;
    }

    @Override
    public DoubleWrapper add(NumericType<Double, DoubleWrapper> other) {
        return new DoubleWrapper(val + other.getVal());
    }

    @Override
    public DoubleWrapper subtract(NumericType<Double, DoubleWrapper> other) {
        return new DoubleWrapper(val - other.getVal());
    }

    @Override
    public DoubleWrapper multiply(NumericType<Double, DoubleWrapper> other) {
        return new DoubleWrapper(val * other.getVal());
    }

    @Override
    public DoubleWrapper divide(NumericType<Double, DoubleWrapper> other) {
        return new DoubleWrapper(val / other.getVal());
    }

    @Override
    public DoubleWrapper abs() {
        return new DoubleWrapper(Math.abs(val));
    }

    @Override
    public DoubleWrapper square() {
        return multiply(this);
    }

    @Override
    public DoubleWrapper mod(NumericType<Double, DoubleWrapper> other) {
        return new DoubleWrapper(val % other.getVal());
    }

    @Override
    public DoubleWrapper negate() {
        return new DoubleWrapper(-val);
    }

    @Override
    public Double getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public DoubleWrapper parseConst(String val) {
        return new DoubleWrapper(Double.parseDouble(val));
    }
}
