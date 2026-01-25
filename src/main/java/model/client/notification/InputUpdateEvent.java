package model.client.notification;

import model.client.CurrentSession;

public record InputUpdateEvent(CurrentSession session) implements GameEvent {

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
