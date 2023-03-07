package expression.generic.operations.wrappers;

public class ShortWrapper implements NumericType<Short, ShortWrapper> {
    private final Short val;

    public ShortWrapper(Short val) {
        this.val = val;
    }

    public ShortWrapper(Integer val) {
        this.val = val.shortValue();
    }

    @Override
    public ShortWrapper add(NumericType<Short, ShortWrapper> other) {
        return new ShortWrapper(val + other.getVal());
    }

    @Override
    public ShortWrapper subtract(NumericType<Short, ShortWrapper> other) {
        return new ShortWrapper(val - other.getVal());
    }

    @Override
    public ShortWrapper multiply(NumericType<Short, ShortWrapper> other) {
        return new ShortWrapper(val * other.getVal());
    }

    @Override
    public ShortWrapper divide(NumericType<Short, ShortWrapper> other) {
        return new ShortWrapper(val / other.getVal());
    }

    @Override
    public ShortWrapper abs() {
        return new ShortWrapper(Math.abs(val));
    }

    @Override
    public ShortWrapper square() {
        return multiply(this);
    }

    @Override
    public ShortWrapper mod(NumericType<Short, ShortWrapper> other) {
        return new ShortWrapper(val % other.getVal());
    }

    @Override
    public ShortWrapper negate() {
        return new ShortWrapper(-val);
    }

    @Override
    public Short getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }

    @Override
    public ShortWrapper parseConst(String val) {
        return new ShortWrapper(Short.parseShort(val));
    }
}
