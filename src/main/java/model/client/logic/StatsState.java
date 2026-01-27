package model.client.logic;

import model.client.Feature;
import model.client.message.StatsRequestMessage;
import model.client.notification.GameEventListener;
import model.client.notification.StatsReceivedEvent;
import model.client.notification.TabPressedEvent;
import model.server.message.StatsRequestResponse;

import java.util.EnumSet;
import java.util.Set;

public class StatsState extends ClientState {
    private Set<Feature> FEATURE = EnumSet.of(Feature.STATS);

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param logic the game logic
     */
    public StatsState(ClientGameLogic logic) {
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
    public void received(StatsRequestResponse msg) {
        logic.getEventBroker().notifyListeners(new StatsReceivedEvent(msg));
    }

    @Override
    public void receivedEvent(TabPressedEvent event) {
        logic.setState(new GameOverState(logic));
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
