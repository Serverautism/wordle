package model.client.message;

import model.server.message.ServerMessage;

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
     * Handles a DisconnectEffectMsg message received from the server.
     *
     * @param msg the DisconnectEffectMsg message received
     */
    //void received(DisconnectEffectMsg msg);
}
