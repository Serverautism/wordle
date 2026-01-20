package model.client.message;

import com.jme3.network.serializing.Serializable;
import model.server.message.ClientMessageInterpreter;

/**
 * Message sent to server when trying to authenticate
 */
@Serializable
public class LoginMessage extends ClientMessage{
    private String name;
    private String password;

    /**
     * No-argument constructor for serialization purposes
     */
    private LoginMessage() {}

    /**
     * Creates a new LoginMessage
     */
    public LoginMessage(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

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
