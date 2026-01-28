package model.client.logic;

import model.client.Feature;
import model.general.message.client.LoginMessage;
import model.general.message.client.StartGameMessage;
import model.general.message.server.ConnectionResponse;
import model.general.message.server.LoginResponse;
import model.general.message.server.StartGameResponse;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the state of the client waiting for the
 * authentication confirmation from the server.
 */
public class InitialState extends ClientState{
    private Set<Feature> FEATURE = EnumSet.of(Feature.WORDLE);

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
     * Sends authentication request when server responded to the connection.
     *
     * @param msg the ConnectionResponse message
     */
    @Override
    public void received(ConnectionResponse msg) {
        logic.send(new LoginMessage(logic.getGameConfig().getName(), logic.getGameConfig().getPassword()));
    }

    /**
     * Sets the game details provided by the server.
     *
     * @param msg the game details  including word length
     */
    @Override
    public void received(LoginResponse msg) {
        logic.send(new StartGameMessage());
    }

    @Override
    public void received(StartGameResponse msg) {
        logic.startNewSession(msg.getWordLength(), msg.getAllowedGuesses());
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
