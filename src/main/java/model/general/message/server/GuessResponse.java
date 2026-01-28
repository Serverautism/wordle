package model.general.message.server;

import com.jme3.network.serializing.Serializable;
import model.general.message.client.ServerMessageInterpreter;
import model.general.config.CharacterPosition;

import java.util.Arrays;
import java.util.List;

@Serializable
public class GuessResponse extends ServerMessage {
    private boolean accepted;
    private int[] positionOrdinals;

    /**
     * No-argument constructor for serialization purposes
     */
    private GuessResponse() {}

    /**
     * Construct a new GuessResponse
     *
     * @param accepted if the guess was valid
     * @param positions List containing information about the letter placement
     */
    public GuessResponse(boolean accepted, List<CharacterPosition> positions) {
        this.accepted = accepted;
        positionOrdinals = positions.stream().mapToInt(Enum::ordinal).toArray();
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

    public boolean isAccepted() {
        return accepted;
    }

    public List<CharacterPosition> getPositions() {
        return Arrays.stream(positionOrdinals).mapToObj(i -> CharacterPosition.values()[i]).toList();
    }
}