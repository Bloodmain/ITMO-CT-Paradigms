package expression;

public interface PriorityExpression extends TripleExpression {
    Priority getPriority();
    String getOperatorSymbol();
}
