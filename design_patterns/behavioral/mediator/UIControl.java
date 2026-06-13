package design_patterns.behavioral.mediator;

//Abstract colleague
public interface UIControl {

    void controlChanged(UIControl control);

    void setControlValue(String value);

    String getControlValue();

    String getControlName();
}
