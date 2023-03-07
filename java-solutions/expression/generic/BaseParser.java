package expression.generic;

import expression.exceptions.NoEOFException;
import expression.exceptions.UnexpectedTokenException;

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

    public void consume(String expected) {
        for (int i = 0; i < expected.length(); ++i) {
            consume();
        }
    }

    public void skipWhitespaces() {
        while (Character.isWhitespace(ch)) {
            consume();
        }
    }

    public boolean test(char expected) {
        return ch == expected;
    }

    public boolean checkEOF() {
        return ch == END;
    }

    public String getToken(Predicate<Character> predicate) {
        StringBuilder res = new StringBuilder();
        while (predicate.test(ch)) {
            res.append(consume());
        }
        source.seekBackwards(ch != END ? res.length() + 1 : res.length());
        consume();
        return res.toString();
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
