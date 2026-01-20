package model.client.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code GameEventBroker} is responsible for managing and dispatching
 * game-related events to all registered {@link GameEventListener} instances.
 */
public class GameEventBroker {
    /**
     * A list of all registered {@link GameEventListener} instances.
     */
    private final List<GameEventListener> listeners = new ArrayList<>();

    /**
     * Registers a new {@link GameEventListener} so that it receives future events.
     * The listener is only added if it is non-null and not already registered.
     *
     * @param listener the listener to register
     */
    public synchronized void addListener(GameEventListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Unregisters a previously added {@link GameEventListener}.
     * The listener is only removed if it is currently registered.
     *
     * @param listener the listener to remove
     */
    public synchronized void removeListener(GameEventListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    /**
     * Notifies all registered listeners of a {@link GameEvent}.
     * Uses a defensive copy of the listener list to ensure safe iteration.
     *
     * @param event the game-level event to dispatch
     */
    public void notifyListeners(GameEvent event) {
        listenersCopy().forEach(event::notifyListener);
    }

    /**
     * Creates a thread-safe defensive copy of the current listener list.
     * This avoids concurrent modification exceptions if listeners are added or
     * removed while events are being dispatched.
     *
     * @return a new {@link List} containing all registered listeners
     */
    private synchronized List<GameEventListener> listenersCopy() {
        return new ArrayList<>(listeners);
    }
}
