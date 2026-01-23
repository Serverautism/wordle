package model.server.message;

import model.client.message.DisconnectMessage;
import model.client.message.GuessMessage;
import model.client.message.LoginMessage;
import model.client.message.StartGameMessage;

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

    /**
     * Processes a received StartGameMessage.
     *
     * @param msg the StartGameMessage to be processed
     * @param id  the connection ID from which the message was received
     */
    void received(StartGameMessage msg, int id);

    /**
     * Processes a received GuessMessage.
     *
     * @param msg the GuessMessage to be processed
     * @param id  the connection ID from which the message was received
     */
    void received(GuessMessage msg, int id);

    /**
     * Processes a received DisconnectMessage.
     *
     * @param msg the DisconnectMessage to be processed
     * @param id  the connection ID from which the message was received
     */
    void received(DisconnectMessage msg, int id);
}
