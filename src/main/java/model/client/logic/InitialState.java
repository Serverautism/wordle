package model.client.logic;

import model.client.Feature;
import model.client.message.LoginMessage;

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

//    /**
//     * Sets the game details provided by the server.
//     *
//     * @param details the game details including map size and ships
//     */
//    @Override
//    public void received(GameDetails details) {
//        logic.initialize(details);
//        logic.showInfoText(details.getInfoTextKey());
//        logic.setState(new TeamEditorState(logic));
//    }

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
