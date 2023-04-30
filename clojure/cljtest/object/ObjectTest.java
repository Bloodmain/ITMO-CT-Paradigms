package cljtest.object;

import base.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ObjectTest {
    public static final Selector SELECTOR = ObjectTester.builder()
            .variant("Base", NARY_ARITH)
            .variant("MeansqRMS",       MEANSQ,      RMS)
            .variant("SumexpLSE",       SUMEXP,      LSE)
            .selector();

    private ObjectTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
