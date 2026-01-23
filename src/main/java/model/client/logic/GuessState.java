package model.client.logic;

import model.client.message.GuessMessage;
import model.client.notification.BackspacePressedEvent;
import model.client.notification.EnterPressedEvent;
import model.client.notification.LetterPressedEvent;

/**
 * Represents the state where the client is able to submit a guess
 */
public class GuessState extends ClientState {
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
    }

    @Override
    public void receivedEvent(LetterPressedEvent event) {

    }

    @Override
    public void receivedEvent(EnterPressedEvent event) {
        logic.send(new GuessMessage());
    }

    @Override
    public void receivedEvent(BackspacePressedEvent event) {

    }
}
