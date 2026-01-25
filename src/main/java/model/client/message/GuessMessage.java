package model.client.message;

import com.jme3.network.serializing.Serializable;
import model.server.message.ClientMessageInterpreter;

/**
 * Message sent to server when trying to make a guess
 */
@Serializable
public class GuessMessage extends ClientMessage {
    private String guess;

    /**
     * No-argument constructor for serialization purposes
     */
    private GuessMessage() {}

    /**
     * Creates new GuessMessage
     *
     * @param guess the word that is being guessed
     */
    public GuessMessage(String guess) {
        this.guess = guess;
    }

    public String getGuess() {
        return guess;
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
