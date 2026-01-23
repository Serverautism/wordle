package model.server.message;

import com.jme3.network.serializing.Serializable;
import model.client.message.ServerMessageInterpreter;

@Serializable
public class StartGameResponse extends ServerMessage {
    /**
     * Length of the games answer
     */
    private int wordLength;

    /**
     * No-argument constructor for serialization purposes
     */
    private StartGameResponse() {}

    /**
     * Construct a new StartGameResponse indicating that a game was started.
     * Contains the length of the answer word.
     *
     * @param wordLength length of the correct answer
     */
    public StartGameResponse(int wordLength) {
        this.wordLength = wordLength;
    }

    /**
     * Returns the length of the correct answer for this game
     *
     * @return the length of the answer as int
     */
    public int getWordLength() {
        return wordLength;
    }

    /**
     * Returns the maximum amount of guesses for this wordle
     *
     * @return the number of guesses as int
     */
    public int getAllowedGuesses() {
        return 6;
    }

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
