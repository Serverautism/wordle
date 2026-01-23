package client;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import model.client.notification.GameEventBroker;
import model.client.notification.LetterPressedEvent;

public class InputState extends AbstractAppState {
    /**
     * Logger for events and errors
     */
    private static final System.Logger LOGGER = System.getLogger(InputState.class.getName());

    /**
     * Reference to the main application.
     */
    private WordleApp app;

    /**
     * Initializes this state and sets up the Escape key input mapping and listener.
     *
     * @param stateManager the {@link AppStateManager} managing this state
     * @param app          the game app instance
     */
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (WordleApp) app;
        app.getInputManager().addListener((ActionListener) this::letterPressed, "A", "B");
    }

    private void createMappings() {
        InputManager im = app.getInputManager();
        im.addMapping("A", new KeyTrigger(KeyInput.KEY_A));

    }

    private void letterPressed(String name, boolean isPressed, float tpf) {
        GameEventBroker eb = app.getGameLogic().getEventBroker();
        if (isPressed) {
            eb.notifyListeners(new LetterPressedEvent(name));
        }
    }
}
