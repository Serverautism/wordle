package client;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import model.client.logic.ClientGameLogic;

/**
 * Abstract class representing a state in the game.
 * Extends the AbstractAppState from jMonkeyEngine to manage state behavior.
 */
public abstract class GameAppState extends AbstractAppState {
    /**
     * Reference to the main app.
     */
    private WordleApp app;

    /**
     * Creates a new GameAppState that is initially disabled.
     *
     * @see #setEnabled(boolean)
     */
    protected GameAppState() {
        setEnabled(false);
    }

    /**
     * Initializes the state manager and application.
     *
     * @param stateManager The state manager
     * @param application  The application instance
     */
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        this.app = (WordleApp) application;
        if (isEnabled()) enableState();
    }

    /**
     * Returns the GameApp instance associated with this GameAppState.
     *
     * @return The GameApp instance.
     */
    public WordleApp getApp() {
        return app;
    }

    /**
     * Returns the client game logic handler.
     *
     * @return the client game logic handler
     */
    public ClientGameLogic getGameLogic() {
        return app.getGameLogic();
    }

    /**
     * Sets the enabled state of the GameAppState.
     * If the new state is the same as the current state, the method returns.
     *
     * @param enabled The new enabled state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled) return;
        super.setEnabled(enabled);
        if (app != null) {
            if (enabled)
                enableState();
            else
                disableState();
        }
    }

    /**
     * This method is called when the state is enabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is enabled.
     */
    protected abstract void enableState();

    /**
     * This method is called when the state is disabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is disabled.
     */
    protected abstract void disableState();
}
