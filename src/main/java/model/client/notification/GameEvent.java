package model.client.notification;

/**
 * Basic Inteface for all GameEvents
 */
public interface GameEvent {

    /**
     * Notifies and passes GameEvent to a Listener
     *
     * @param listener the specific Listener
     */
    void notifyListener(GameEventListener listener);
}
