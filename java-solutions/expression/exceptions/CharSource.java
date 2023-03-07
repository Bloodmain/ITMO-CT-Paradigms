package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    void seekBackwards(int offset);
    int getPos();
}
