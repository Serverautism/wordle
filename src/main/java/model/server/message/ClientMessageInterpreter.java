package model.server.message;

import model.client.message.LoginMessage;

/**
 * Visitor interface for processing all client messages.
 */
public interface ClientMessageInterpreter {

    /**
     * Processes a received LoginMessage.
     *
     * @param msg the LoginMessage to be processed
     * @param id  the connection ID from which the message was received
     */
    void received(LoginMessage msg, int id);
}
