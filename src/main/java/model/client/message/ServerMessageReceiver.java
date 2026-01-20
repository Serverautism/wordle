package model.client.message;

import model.server.message.ServerMessage;

/**
 * An interface for sending a message from the server to a client.
 */
public interface ServerMessageReceiver {
    /**
     * First called when a ServerMessage is received.
     *
     * @param msg the message that is received
     */
    void receive(ServerMessage msg);
}
