package design_patterns.behavioral.interpreter;

//Abstract expression
public interface PermissionExpression {

    boolean interpret(User user);
}
