package jstest.expression;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Dialect {
    private final String variable;
    private final String constant;
    private final BiFunction<String, List<String>, String> nary;
    private final Function<String, String> renamer;

    private Dialect(
            final String variable,
            final String constant,
            final BiFunction<String, List<String>, String> nary,
            final Function<String, String> renamer
    ) {
        this.variable = variable;
        this.constant = constant;
        this.nary = nary;
        this.renamer = renamer;
    }

    public Dialect(final String variable, final String constant, final BiFunction<String, List<String>, String> nary) {
        this(variable, constant, nary, Function.identity());
    }

    public Dialect(final String variable, final String constant, final String operation, final String separator) {
        this(
                variable,
                constant,
                (op, args) -> operation.replace("{op}", op).replace("{args}", String.join(separator, args))
        );
    }

    public Dialect renamed(final Function<String, String> renamer) {
        return new Dialect(variable, constant, nary, this.renamer.compose(renamer));
    }

    public String variable(final String name) {
        return String.format(variable, name);
    }

    public String constant(final int value) {
        return String.format(constant, value);
    }

    public static String nullary(final String name) {
        return name;
    }

    public String operation(final String name, final List<String> as) {
        return nary.apply(renamer.apply(name), as);
    }

    public Dialect functional() {
        return renamed(Dialect::toFunctional);
    }

    private static String toFunctional(final String name) {
        return name.chars().allMatch(Character::isUpperCase)
            ? name.toLowerCase()
            : Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
