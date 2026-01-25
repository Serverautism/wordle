package model.client.message;

import model.server.message.*;

/**
 * An interface for processing server messages.
 * Implementations of this interface can be used to handle different types of server messages.
 */
public interface ServerMessageInterpreter extends ServerMessageReceiver {
    /**
     * First called when a ServerMessage is received.
     *
     * @param msg the message that is received
     */
    @Override
    default void receive(ServerMessage msg) {
        msg.accept(this);
    }

    /**
     * Handles a StartGameResponse message received from the server.
     *
     * @param msg the StartGameResponse message received
     */
    void received(StartGameResponse msg);

    /**
     * Handles a LoginResponse message received from the server.
     *
     * @param msg the LoginResponse message received
     */
    void received(LoginResponse msg);

    /**
     * Handles a DisconnectResponse message received from the server.
     *
     * @param msg the DisconnectResponse message received
     */
    void received(DisconnectResponse msg);

    /**
     * Handles a GuessResponse message received from the server.
     *
     * @param msg the GuessResponse message received
     */
    void received(GuessResponse msg);
}
