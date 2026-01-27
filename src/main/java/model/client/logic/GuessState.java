package model.client.logic;

import model.client.Feature;
import model.client.message.GuessMessage;
import model.client.message.StatsRequestMessage;
import model.client.notification.*;
import model.general.config.CharacterPosition;
import model.server.message.GuessResponse;

import java.util.EnumSet;
import java.util.Set;

/**
 * Represents the state where the client is able to submit a guess
 */
public class GuessState extends ClientState {
    private Set<Feature> FEATURE = EnumSet.of(Feature.WORDLE);

    /**
     * Constructs a new GuessState.
     *
     * @param logic the game logic
     */
    public GuessState(ClientGameLogic logic) {
        super(logic);
    }

    @Override
    public void entry() {
        logic.getEventBroker().addListener(this);
        logic.getEventBroker().notifyListeners(new StartGameEvent(logic.getCurrentSession()));
    }

    @Override
    public void exit() {
        logic.getEventBroker().removeListener(this);
    }

    @Override
    public void received(GuessResponse msg) {
        if (msg.isAccepted()) {
            ClientGameLogic.LOGGER.log(System.Logger.Level.INFO, "guess {0} was accepted with result: {1}", logic.getCurrentSession().getUnsubmittedGuess(), msg.getPositions());
            logic.getCurrentSession().submitGuess(msg.getPositions());
            logic.getEventBroker().notifyListeners(new GuessSubmittedEvent(logic.getCurrentSession()));
            if (msg.getPositions().stream().allMatch(c -> c == CharacterPosition.RIGHT)) {
                logic.setState(new GameOverState(logic));
            } else if (logic.getCurrentSession().remainingGuesses() <= 0) {
                logic.setState(new GameOverState(logic));
            }
        } else {
            ClientGameLogic.LOGGER.log(System.Logger.Level.INFO, "guess {0} was rejected", logic.getCurrentSession().getUnsubmittedGuess());
        }
    }

    @Override
    public void receivedEvent(LetterPressedEvent event) {
        logic.getCurrentSession().addCharacter(event.letter());
        ClientGameLogic.LOGGER.log(System.Logger.Level.INFO, event.letter());
        ClientGameLogic.LOGGER.log(System.Logger.Level.INFO, logic.getCurrentSession().getUnsubmittedGuess());
        logic.getEventBroker().notifyListeners(new InputUpdateEvent(logic.getCurrentSession()));
    }

    @Override
    public void receivedEvent(EnterPressedEvent event) {
        if (logic.getCurrentSession().isCurrentGuessValid()) {
            logic.send(new GuessMessage(logic.getCurrentSession().getUnsubmittedGuess()));
        }
    }

    @Override
    public void receivedEvent(BackspacePressedEvent event) {
        logic.getCurrentSession().removeLastCharacter();
        ClientGameLogic.LOGGER.log(System.Logger.Level.INFO, logic.getCurrentSession().getUnsubmittedGuess());
        logic.getEventBroker().notifyListeners(new InputUpdateEvent(logic.getCurrentSession()));
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
