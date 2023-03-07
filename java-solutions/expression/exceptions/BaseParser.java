package expression.exceptions;

import java.util.function.Predicate;

public class BaseParser {
    protected final static char END = '\0';
    protected final CharSource source;
    protected char ch;


    public BaseParser(CharSource source) {
        this.source = source;
        consume();
    }

    public char consume() {
        char now = ch;
        ch = source.hasNext() ? source.next() : END;
        return now;
    }

    public void skipWhitespaces() {
        while (Character.isWhitespace(ch)) {
            consume();
        }
    }

    public boolean test(char expected) {
        return ch == expected;
    }

    public boolean test(Predicate<Character> predicate) {
        return predicate.test(ch);
    }

    public boolean checkEOF() {
        return ch == END;
    }

    public boolean testAndConsume(char expected) {
        if (test(expected)) {
            consume();
            return true;
        }
        return false;
    }

    public boolean testAndConsume(String expected) {
        for (int i = 0; i < expected.length(); ++i) {
            if (!testAndConsume(expected.charAt(i))) {
                if (i != 0) {
                    seekBackwards(i);
                }
                return false;
            }
        }
        return true;
    }

    public String getSatisfied(Predicate<Character> predicate) {
        StringBuilder res = new StringBuilder();
        while (predicate.test(ch)) {
            res.append(consume());
        }
        return res.toString();
    }

    public void seekBackwards(int offset) {
        source.seekBackwards(offset + 1);
        ch = source.next();
    }

    public boolean checkBounds(char leftBound, char rightBound) {
        return leftBound <= ch && ch <= rightBound;
    }

    public void assertEOF() throws NoEOFException {
        if (!checkEOF()) {
            throw new NoEOFException("Pos " + source.getPos() + ": expected EOF, but found '" + ch + "'");
        }
    }

    public void assertNextEquals(char c) throws UnexpectedTokenException {
        if (!test(c)) {
            throw new UnexpectedTokenException("Pos " + source.getPos() + ": expected '" + c + "', but found '" + ch + "'");
        }
    }
}
