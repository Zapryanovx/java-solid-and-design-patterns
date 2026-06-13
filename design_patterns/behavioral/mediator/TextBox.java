package design_patterns.behavioral.mediator;

public class TextBox implements UIControl {

    private String text = "Lorem ipsum";
    private final UIMediator mediator;

    public TextBox(UIMediator mediator) {
        this.mediator = mediator;
        this.mediator.register(this);
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
    public String getControlValue() {
        return text;
    }

    @Override
    public String getControlName() {
        return "TextBox";
    }
}
