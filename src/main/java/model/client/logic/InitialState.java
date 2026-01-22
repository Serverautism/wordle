package model.client.logic;

import model.client.Feature;
import model.client.message.LoginMessage;
import model.client.message.StartGameMessage;
import model.server.message.LoginResponse;
import model.server.message.StartGameResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents the state of the client waiting for the
 * authentication confirmation from the server.
 */
public class InitialState extends ClientState{
    private Set<Feature> FEATURE = new HashSet<>();

    /**
     * Constructs a new InitialState with the specified ClientGameLogic.
     *
     * @param logic the ClientGameLogic associated with this state
     */
    public InitialState(ClientGameLogic logic) {
        super(logic);
    }

    @Override
    public void entry() {
    }

    /**
     * Sets the game details provided by the server.
     *
     * @param msg the game details  including word length
     */
    @Override
    public void received(LoginResponse msg) {
        logic.setState(new GuessState(logic));
    }

    /**
     * Returns the set of all features of this state.
     *
     * @return a set of features.
     */
    @Override
    public Set<Feature> getFeatures() {
        return FEATURE;
    }
}
