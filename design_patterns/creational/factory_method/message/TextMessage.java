package design_patterns.creational.factory_method.message;

public class TextMessage extends Message {

    @Override
    public String getContent() {
        return "Text";
    }
}
