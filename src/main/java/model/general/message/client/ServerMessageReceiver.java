package model.general.message.client;

import model.general.message.server.ServerMessage;

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
