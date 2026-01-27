package model.client.message;

import com.jme3.network.serializing.Serializable;
import model.server.message.ClientMessageInterpreter;

@Serializable
public class StatsRequestMessage extends ClientMessage {
    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     * @param id          the connection ID of the sender
     */
    @Override
    public void accept(ClientMessageInterpreter interpreter, int id) {
        interpreter.received(this, id);
    }
}
