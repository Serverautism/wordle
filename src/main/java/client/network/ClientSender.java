package client.network;

import model.client.message.ClientMessage;

/**
 * Interface for sending messages to the server.
 */
public interface ClientSender {

    /**
     * Send a message to the server.
     * @param msg the message to send
     */
    public void send(ClientMessage msg);
}
