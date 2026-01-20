package server.network;

import model.server.message.ServerMessage;

/**
 * Interface for sending messages to a client.
 */
public interface ServerSender {
    /**
     * Send the specified message to the client.
     *
     * @param id      the id of the client that shall receive the message
     * @param message the message
     */
    void send(int id, ServerMessage message);
}
