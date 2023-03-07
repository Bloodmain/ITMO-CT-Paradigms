package jstest.functional;

import base.Selector;
import jstest.expression.Builder;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class FunctionalTest {
    private FunctionalTest() {
    }

    /* package-private */ static Selector.Composite<Builder> selector() {
        return Builder.selector(
                FunctionalTest.class,
                mode -> false,
                (builder, counter) -> ExpressionTest.tester(counter, builder.language(
                        ExpressionTest.ARITHMETIC,
                        builder.dialect(ExpressionTest.POLISH, alias -> alias.chars().noneMatch(Character::isLetter))
                )),
                "easy", "hard"
        );
    }

    public static final Selector SELECTOR = selector()
            .variant("Base")
            .selector();

    public static void main(final String... args) {
        SELECTOR.main(args);
    }
}
