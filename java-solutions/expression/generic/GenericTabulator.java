package expression.generic;

import expression.exceptions.ParseException;
import expression.generic.exceptions.UnsupportedModeException;
import expression.generic.operations.GenericExpression;
import expression.generic.operations.wrappers.*;

import java.math.BigInteger;
import java.util.function.Function;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(
            String mode,
            String expression,
            int x1, int x2, int y1, int y2, int z1, int z2
    ) throws Exception {
        return switch (mode) {
            case "i" -> new TabulateCertainMode<Integer, CheckedIntegerWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    CheckedIntegerWrapper::new
            );
            case "d" -> new TabulateCertainMode<Double, DoubleWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    DoubleWrapper::new
            );
            case "bi" -> new TabulateCertainMode<BigInteger, BigIntWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    BigIntWrapper::new
            );
            case "u" -> new TabulateCertainMode<Integer, IntegerWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    IntegerWrapper::new
            );
            case "p" -> new TabulateCertainMode<Integer, PrimeWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    PrimeWrapper::new
            );
            case "s" -> new TabulateCertainMode<Short, ShortWrapper>().genericTabulate(
                    x1, x2, y1, y2, z1, z2,
                    expression,
                    ShortWrapper::new
            );
            default -> throw new UnsupportedModeException("Unsupported mode " + mode);
        };
    }

    private static class TabulateCertainMode<T, W extends NumericType<T, W>> {
        public Object[][][] genericTabulate(int x1, int x2, int y1, int y2, int z1, int z2,
                                            String expression,
                                            Function<Integer, W> intWrapper
        ) throws ParseException {
            GenericExpression<T, W> expr = new GenericExpressionParser<T, W>().parse(expression);
            Object[][][] res = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
            for (int x = x1; x <= x2; ++x) {
                for (int y = y1; y <= y2; ++y) {
                    for (int z = z1; z <= z2; ++z) {
                        try {
                            res[x - x1][y - y1][z - z1] = expr.evaluate(
                                    intWrapper.apply(x),
                                    intWrapper.apply(y),
                                    intWrapper.apply(z)
                            ).getVal();
                        } catch (ArithmeticException ignored) {
                        }
                    }
                }
            }
            return res;
        }
    }
}
