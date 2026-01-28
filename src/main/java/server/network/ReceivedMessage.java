package server.network;


import model.general.message.client.ClientMessage;
import model.server.logic.ServerGameLogic;

/**
 * Encapsulates a client message together with the sender’s connection identifier.
 * <p>
 * Instances of this record carry the raw {@link ClientMessage} and the integer
 * connection ID of the client that sent it. Invoke {@link #process(ServerGameLogic)}
 * to dispatch the message into the server’s game logic.
 * </p>
 *
 * @param message the client message to be processed
 * @param from    the connection ID of the client that sent the message
 */

public record ReceivedMessage(ClientMessage message, int from) {

    /**
     * Dispatches the encapsulated message to the specified {@link ServerGameLogic}.
     * <p>
     * This calls {@code logic.receive(message, from)}, allowing the game logic
     * to interpret and handle the message according to its current state.
     * </p>
     *
     * @param logic the server game logic that will process the message
     */

    public void process(ServerGameLogic logic) {
        logic.receive(message, from);
    }
}
