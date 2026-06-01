package design_patterns.creational.factory_method;

import design_patterns.creational.factory_method.message.Message;
import design_patterns.creational.factory_method.message.TextMessage;

/**
 * Provides implementation for creating Text messages
 */
public class TextMessageCreator extends MessageCreator {

    @Override
    public Message createMessage() {
        return new TextMessage();
    }
}
