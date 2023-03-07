package expression.generic;

import expression.generic.operations.*;
import expression.exceptions.*;
import expression.generic.operations.wrappers.NumericType;

import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public final class GenericExpressionParser<T, W extends NumericType<T, W>> {
    public GenericExpression<T, W> parse(CharSource source) throws ParseException {
        return new ExpressionAnalyzer<T, W>(source).parse();
    }

    public GenericExpression<T, W> parse(String source) throws ParseException {
        return parse(new StringSource(source));
    }

    private static class ExpressionAnalyzer<T, W extends NumericType<T, W>> extends BaseParser {
        private final Map<String, BinaryOperator<GenericExpression<T, W>>> ADDICTIVE = Map.of(
                "+", Add::new,
                "-", Subtract::new
        );

        private final Map<String, BinaryOperator<GenericExpression<T, W>>> MULTIPLICATIVE = Map.of(
                "*", Multiply::new,
                "/", Divide::new,
                "mod", Mod::new
        );

        private final Map<String, UnaryOperator<GenericExpression<T, W>>> UNARY = Map.of(
                "-", Negate::new,
                "abs", Abs::new,
                "square", Square::new
        );

        private static final Set<String> AVAILABLE_VARIABLES = Set.of(
                "x", "y", "z"
        );

        public ExpressionAnalyzer(CharSource source) {
            super(source);
        }

        private GenericExpression<T, W> parse() throws ParseException {
            GenericExpression<T, W> result = parseAdditive();
            assertEOF();
            return result;
        }

        private GenericExpression<T, W> parseExpression(
                final Map<String, BinaryOperator<GenericExpression<T, W>>> operationsToParse,
                final SupplierException<GenericExpression<T, W>, ParseException> next
        ) throws ParseException {
            GenericExpression<T, W> expr = next.get();
            skipWhitespaces();
            String op = readOperation();
            while (operationsToParse.containsKey(op)) {
                consume(op);
                GenericExpression<T, W> right = next.get();
                skipWhitespaces();
                expr = operationsToParse.get(op).apply(expr, right);
                op = readOperation();
            }
            return expr;
        }

        private GenericExpression<T, W> parseAdditive() throws ParseException {
            return parseExpression(ADDICTIVE, this::parseMultiplicative);
        }

        private GenericExpression<T, W> parseMultiplicative() throws ParseException {
            return parseExpression(MULTIPLICATIVE, this::parseBrackets);
        }

        private GenericExpression<T, W> parseBrackets() throws ParseException {
            skipWhitespaces();
            if (test('(')) {
                consume();
                GenericExpression<T, W> expr = parseAdditive();
                skipWhitespaces();
                try {
                    assertNextEquals(')');
                } catch (UnexpectedTokenException e) {
                    throw new UnclosedParenthesesException(e.getMessage());
                }
                consume();
                return expr;
            } else {
                return parseUnary();
            }
        }

        private GenericExpression<T, W> parseUnary() throws ParseException {
            skipWhitespaces();
            if (checkBounds('0', '9')) {
                return parseConst(false);
            } else {
                String name = readOperation();
                consume(name);
                if (name.equals("-") && checkBounds('0', '9')) {
                    return parseConst(true);
                } else if (UNARY.containsKey(name)) {
                    return UNARY.get(name).apply(parseBrackets());
                } else if (AVAILABLE_VARIABLES.contains(name)) {
                    return new Variable<>(name);
                }
                throw new UnavailableIdentifierException(
                        "Pos " + source.getPos() + ": Unavailable operator/variable '" + name + "'"
                );
            }
        }

        private GenericExpression<T, W> parseConst(boolean negative) throws ParseException {
            StringBuilder res = new StringBuilder();
            if (negative) {
                res.append('-');
            }
            while (checkBounds('0', '9')) {
                res.append(consume());
            }
            try {
                return new Const<>(res.toString());
            } catch (NumberFormatException e) {
                throw new BadConstantException("Pos " + source.getPos() + ": Unavailable const '" + res + "'");
            }
        }

        private String readOperation() {
            String op = readSymbolicName();
            if (op.isEmpty()) {
                op = readAlphabeticName();
            }
            return op;
        }

        private String readSymbolicName() {
            if (test('+')) {
                return "+";
            } else if (test('-')) {
                return "-";
            } else if (test('*')) {
                return "*";
            } else if (test('/')) {
                return "/";
            }
            return "";
        }

        private String readAlphabeticName() {
            return getToken(c -> Character.isLetter(c) || Character.isDigit(c));
        }
    }
}