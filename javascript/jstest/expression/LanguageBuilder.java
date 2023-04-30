package jstest.expression;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface LanguageBuilder {
    Lang getLang();

    Language language(Dialect parsed, Dialect unparsed);
}
