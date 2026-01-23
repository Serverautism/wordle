package model.client.notification;

public record EnterPressedEvent() implements GameEvent {
    /**
     * Notifies and passes GameEvent to a Listener
     *
     * @param listener the specific Listener
     */
    @Override
    public void notifyListener(GameEventListener listener) {
        listener.receivedEvent(this);
    }
}
