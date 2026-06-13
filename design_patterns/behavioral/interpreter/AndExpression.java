package design_patterns.behavioral.interpreter;

//Non-terminal expression
public class AndExpression implements PermissionExpression {

    private final PermissionExpression left;
    private final PermissionExpression right;

    public AndExpression(
            PermissionExpression left, PermissionExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean interpret(User user) {
        return left.interpret(user) && right.interpret(user);
    }

    @Override
    public String toString() {
        return left + " AND " + right;
    }
}
