package expression.exceptions;

import expression.PriorityExpression;

public interface CheckedExpression extends PriorityExpression {
    CheckResult check(int... operands);
}
