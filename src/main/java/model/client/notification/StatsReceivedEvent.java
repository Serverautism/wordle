package model.client.notification;

import model.server.message.StatsRequestResponse;

public record StatsReceivedEvent(StatsRequestResponse msg) implements GameEvent {
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
