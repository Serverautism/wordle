package model.client.notification;

/**
 * The {@code GameEventListener} interface defines callback methods that are
 * triggered whenever a new game event is received by the client.
 * <p>
 * Implementing classes can react to these events by processing incoming
 * message or sound events, allowing the game to update UI elements,
 * play audio effects, or adjust internal logic accordingly.
 * </p>
 */
public interface GameEventListener {
    /**
     * Called when a {@link LetterPressedEvent} has been received.
     *
     * @param event the received LetterPressedEvent containing information
     *              about the letter that was pressed
     */
    default void receivedEvent(LetterPressedEvent event) {}

    /**
     * Called when a {@link EnterPressedEvent} has been received.
     *
     * @param event the received EnterPressedEvent
     */
    default void receivedEvent(EnterPressedEvent event) {}

    /**
     * Called when a {@link BackspacePressedEvent} has been received.
     *
     * @param event the received BackspacePressedEvent
     */
    default void receivedEvent(BackspacePressedEvent event) {}

    /**
     * Called when a {@link StartGameEvent} has been received.
     *
     * @param event the received StartGameEvent
     */
    default void receivedEvent(StartGameEvent event) {}

    /**
     * Called when a {@link GuessSubmittedEvent} has been received.
     *
     * @param event the received GuessSubmittedEvent
     */
    default void receivedEvent(GuessSubmittedEvent event) {}

    /**
     * Called when a {@link InputUpdateEvent} has been received.
     *
     * @param event the received InputUpdateEvent
     */
    default void receivedEvent(InputUpdateEvent event) {}

    /**
     * Called when a {@link TabPressedEvent} has been received.
     *
     * @param event the received TabPressedEvent
     */
    default void receivedEvent(TabPressedEvent event) {}

    /**
     * Called when a {@link StatsReceivedEvent} has been received.
     *
     * @param event the received StatsReceivedEvent
     */
    default void receivedEvent(StatsReceivedEvent event) {}
}