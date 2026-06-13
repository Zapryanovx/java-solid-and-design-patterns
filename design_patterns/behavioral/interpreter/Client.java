package design_patterns.behavioral.interpreter;

public class Client {

    public static void main(String[] args) {
		Report report = new Report(
				"Cashdlow report",
				"NOT FINANCE_ADMIN"
		);

		ExpressionBuilder builder = new ExpressionBuilder();
		PermissionExpression exp = builder.build(report);
    	System.out.println(exp);

		User user = new User(
				"Dave",
				"USER",
				"ADMIN"
		);

		System.out.println("User access report: " + exp.interpret(user));
	}
}
