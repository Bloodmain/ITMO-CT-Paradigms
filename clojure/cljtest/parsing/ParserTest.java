package cljtest.parsing;

import base.Selector;

import static jstest.expression.Operations.NARY_ARITH;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class ParserTest {
    private static final Selector SELECTOR = ParserTester.builder()
            .variant("Base", NARY_ARITH)
            .selector();

    private ParserTest() {
    }

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
