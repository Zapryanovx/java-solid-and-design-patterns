package design_patterns.behavioral.mediator;

public class Client {

    public static void main(String[] args) {
		UIMediator mediator = new UIMediator();

        UIControl c1 = new Label(mediator);
        UIControl c2 = new TextBox(mediator);

        System.out.println(c2.getControlValue());
        c1.setControlValue("Hello");
        System.out.println(c2.getControlValue());
    }
}
