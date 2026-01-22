package model.client.logic;

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
}
