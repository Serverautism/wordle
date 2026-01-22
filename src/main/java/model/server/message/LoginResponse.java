package model.server.message;

import com.jme3.network.serializing.Serializable;
import model.client.message.ServerMessageInterpreter;

@Serializable
public class LoginResponse extends ServerMessage {
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
