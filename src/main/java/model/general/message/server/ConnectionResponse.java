package model.general.message.server;

import com.jme3.network.serializing.Serializable;
import model.general.message.client.ServerMessageInterpreter;

@Serializable
public class ConnectionResponse extends ServerMessage{
    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     */
    @Override
    public void accept(ServerMessageInterpreter interpreter) {
        interpreter.received(this);
    }
}
