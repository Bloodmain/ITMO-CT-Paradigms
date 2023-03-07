package expression.exceptions;

import expression.*;

import java.util.Map;
import java.util.Set;

public final class ExpressionParser implements TripleParser {
    private static final String NEGATE = "--";
    private static final Map<String, Integer> OPERATION_PRIORITIES = Map.of(
            "+", 5,
            "-", 5,
            "*", 10,
            "/", 10,
            NEGATE, 15,
            "reverse", 15,
            "pow10", 15,
            "log10", 15,
            "gcd", 0,
            "lcm", 0
    );

    private static final Set<String> AVAILABLE_VARIABLES = Set.of(
            "x", "y", "z"
    );

    public CheckedExpression parse(CharSource source) throws ParseException {
        return new ExpressionAnalyzer(source).parse();
    }

    @Override
    public CheckedExpression parse(String source) throws ParseException {
        return parse(new StringSource(source));
    }

    private static class ExpressionAnalyzer extends BaseParser {

        public ExpressionAnalyzer(CharSource source) {
            super(source);
        }

        private CheckedExpression parse() throws ParseException {
            CheckedExpression res = parseExpression(-1, null);
            assertEOF();
            return res;
        }

        private CheckedExpression parseExpression(final int returnOnPriority, CheckedExpression left)
                throws ParseException {
            skipWhitespaces();
            if (left == null) {
                if (testAndConsume('(')) {
                    left = parseExpression(-1, null);
                    try {
                        assertNextEquals(')');
                    } catch (UnexpectedTokenException e) {
                        throw new UnclosedParenthesesException(e.getMessage());
                    }
                    assertNextEquals(')');
                    consume();
                } else if (testAndConsume('-')) {
                    if (checkBounds('0', '9')) {
                        left = parseConst(true);
                    } else {
                        skipWhitespaces();
                        left = parseUnary(NEGATE);
                    }
                } else if (test(Character::isAlphabetic)) {
                    String name = parseAlphabeticName();
                    if (AVAILABLE_VARIABLES.contains(name)) {
                        left = new Variable(name);
                    } else {
                        left = parseUnary(name);
                    }
                } else if (checkBounds('0', '9')) {
                    left = parseConst(false);
                } else {
                    throw new UnavailableIdentifierException(
                            "Pos " + source.getPos() + ": Unavailable operand identifier '" + consume() + "'"
                    );
                }
            }

            skipWhitespaces();
            String op = parseSymbolicName();
            if (op.isEmpty()) {
                op = parseAlphabeticName();
            }
            if (op.isEmpty()) {
                return left;
            }

            if (!OPERATION_PRIORITIES.containsKey(op)) {
                throw new UnavailableIdentifierException(
                        "Pos " + source.getPos() + ": Unavailable binary operation's name '" + op + "'"
                );
            }

            if (OPERATION_PRIORITIES.get(op) <= returnOnPriority) {
                seekBackwards(op.length());
                return left;
            }

            skipWhitespaces();
            CheckedExpression right = parseExpression(OPERATION_PRIORITIES.get(op), null);
            CheckedExpression res = switch (op) {
                case "+" -> new CheckedAdd(left, right);
                case "-" -> new CheckedSubtract(left, right);
                case "*" -> new CheckedMultiply(left, right);
                case "gcd" -> new CheckedGcd(left, right);
                case "lcm" -> new CheckedLcm(left, right);
                default -> new CheckedDivide(left, right);
            };

            return checkEOF() ? res : parseExpression(returnOnPriority, res);
        }

        private CheckedExpression parseUnary(String name) throws ParseException {
            if (!OPERATION_PRIORITIES.containsKey(name)) {
                throw new UnavailableIdentifierException(
                        "Pos " + source.getPos() + ": Unavailable unary operator/variable 's name '" + name + "'"
                );
            }
            CheckedExpression expr = parseExpression(OPERATION_PRIORITIES.get(name), null);
            return switch (name) {
                case NEGATE -> new CheckedNegate(expr);
                case "reverse" -> new CheckedReverse(expr);
                case "log10" -> new CheckedLog10(expr);
                case "pow10" -> new CheckedPow10(expr);
                default -> throw new UnavailableIdentifierException(
                        "Pos " + source.getPos() + ": Unavailable unary operator/variable 's name '" + name + "'"
                );
            };
        }

        private String parseSymbolicName() {
            if (testAndConsume("+")) {
                return "+";
            } else if (testAndConsume("-")) {
                return "-";
            } else if (testAndConsume("*")) {
                return "*";
            } else if (testAndConsume("/")) {
                return "/";
            }
            return "";
        }

        private String parseAlphabeticName() {
            return getSatisfied(c -> Character.isAlphabetic(c) || Character.isDigit(c));
        }

        private CheckedExpression parseConst(boolean negative) throws ParseException {
            StringBuilder res = new StringBuilder();
            if (negative) {
                res.append('-');
            }
            while (checkBounds('0', '9')) {
                res.append(consume());
            }
            try {
                return new Const(Integer.parseInt(res.toString()));
            } catch (NumberFormatException e) {
                throw new BadConstantException("Pos " + source.getPos() + ": Unavailable const '" + res + "'");
            }
        }
    }
}
