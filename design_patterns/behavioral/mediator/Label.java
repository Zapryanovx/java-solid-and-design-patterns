package design_patterns.behavioral.mediator;

public class Label implements UIControl {

    private String text = "Label Lorem ipsum";
    private final UIMediator mediator;

    public Label(UIMediator mediator) {
        this.mediator = mediator;
        mediator.register(this);
    }

    public void setText(String text) {
        this.text = text;
        mediator.valueChanged(this);
    }

    @Override
    public void controlChanged(UIControl control) {
        this.text = control.getControlValue();
    }

    @Override
    public String getControlName() {
        return "Label";
    }

    @Override
    public String getControlValue() {
        return text;
    }
}
