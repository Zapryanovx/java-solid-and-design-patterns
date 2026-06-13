package design_patterns.behavioral.mediator;

public class Slider implements UIControl {

    private String text = "Lorem ipsum";
    private final UIMediator mediator;

    public Slider(UIMediator mediator) {
        this.mediator = mediator;
        mediator.register(this);
    }

    @Override
    public void setControlValue(String text) {
        this.text = text;
        mediator.valueChanged(this);
    }

    @Override
    public void controlChanged(UIControl control) {
        this.text = control.getControlValue();
    }

    @Override
    public String getControlName() {
        return "Slider";
    }

    @Override
    public String getControlValue() {
        return text;
    }
}
