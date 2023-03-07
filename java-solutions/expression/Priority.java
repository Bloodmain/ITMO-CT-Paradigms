package expression;
public class Priority {
    private final int priority;
    private final int group;
    private final int properties;

    public Priority(int priority, int group, int properties) {
        this.priority = priority;
        this.group = group;
        this.properties = properties;
    }

    public boolean compareLeft(Priority that) {
        return that.group < this.group;
    }

    public boolean compareRight(Priority that) {
        if (that.group < this.group) {
            return true;
        } else if (that.group == this.group) {
            if (that.priority < this.priority) {
                return (this.properties & OperationsProperties.ASSOCIATIVE) == 0;
            }
            if (that.priority == this.priority) {
                return (that.properties & OperationsProperties.REFLEXIVE) == 0;
            }
            return (that.properties & OperationsProperties.HIGH_PRIORITY_IN_GROUP) != 0;
        }
        return false;
    }
}
