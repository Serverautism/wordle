package model.client.message;

import com.jme3.network.AbstractMessage;

public abstract class ClientMessage extends AbstractMessage {
    /**
     * Constructs a new ClientMessage instance.
     */
    protected ClientMessage() {
        super(true);
    }

    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     * @param id        the connection ID of the sender
     */
    public abstract void accept(ClientMessageInterpreter interpreter, int id);
}
