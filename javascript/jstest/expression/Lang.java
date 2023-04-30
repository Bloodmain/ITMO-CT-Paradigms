package jstest.expression;

import base.ExtendedRandom;

import java.util.Map;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Lang {
    Map<String, Integer> getVariableNames();

    ExtendedRandom random();

    boolean hasVarargs();

    Integer getPriority(String op);
}
