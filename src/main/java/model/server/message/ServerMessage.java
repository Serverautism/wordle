package model.server.message;

import com.jme3.network.AbstractMessage;
import model.client.message.ServerMessageInterpreter;

/**
 * An abstract base class for server messages used in network transfer.
 * It extends the AbstractMessage class provided by the jme3-network library.
 */
public abstract class ServerMessage extends AbstractMessage {

    /**
     * Constructs a new ServerMessage instance.
     */
    protected ServerMessage(){};

    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     */
    public abstract void accept(ServerMessageInterpreter interpreter);
}
