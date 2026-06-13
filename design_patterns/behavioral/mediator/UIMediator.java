package design_patterns.behavioral.mediator;

import java.util.ArrayList;
import java.util.List;

//Mediator
public class UIMediator {

    private List<UIControl> colleagues = new ArrayList<>();

    public void register(UIControl control) {
        colleagues.add(control);
    }

    public void valueChanged(UIControl control) {
        colleagues.stream()
                .filter(c -> !c.equals(control))
                .forEach(c -> c.controlChanged(control));
    }
}
