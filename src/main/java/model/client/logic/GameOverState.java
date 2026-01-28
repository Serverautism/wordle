package model.client.logic;

import model.client.Feature;
import model.general.message.client.StartGameMessage;
import model.general.message.client.StatsRequestMessage;
import model.client.notification.EnterPressedEvent;
import model.client.notification.TabPressedEvent;
import model.general.message.server.StartGameResponse;

import java.util.EnumSet;
import java.util.Set;

public class GameOverState extends ClientState {
    private Set<Feature> FEATURE = EnumSet.of(Feature.WORDLE);

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param logic the game logic
     */
    public GameOverState(ClientGameLogic logic) {
        super(logic);
    }

    @Override
    public void entry() {
        logic.getEventBroker().addListener(this);
    }

    @Override
    public void exit() {
        logic.getEventBroker().removeListener(this);
    }

    @Override
    public void received(StartGameResponse msg) {
        logic.startNewSession(msg.getWordLength(), msg.getAllowedGuesses());
        logic.setState(new GuessState(logic));
    }

    @Override
    public void receivedEvent(EnterPressedEvent event) {
        logic.send(new StartGameMessage());
    }

    @Override
    public void receivedEvent(TabPressedEvent event) {
        logic.send(new StatsRequestMessage());
        logic.setState(new StatsState(logic));
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
