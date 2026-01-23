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
        createMappings();
        app.getInputManager().addListener((ActionListener) this::letterPressed, "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    }

    private void createMappings() {
        InputManager im = app.getInputManager();
        im.addMapping("A", new KeyTrigger(KeyInput.KEY_A));
        im.addMapping("B", new KeyTrigger(KeyInput.KEY_B));
        im.addMapping("C", new KeyTrigger(KeyInput.KEY_C));
        im.addMapping("D", new KeyTrigger(KeyInput.KEY_D));
        im.addMapping("E", new KeyTrigger(KeyInput.KEY_E));
        im.addMapping("F", new KeyTrigger(KeyInput.KEY_F));
        im.addMapping("G", new KeyTrigger(KeyInput.KEY_G));
        im.addMapping("H", new KeyTrigger(KeyInput.KEY_H));
        im.addMapping("I", new KeyTrigger(KeyInput.KEY_I));
        im.addMapping("J", new KeyTrigger(KeyInput.KEY_J));
        im.addMapping("K", new KeyTrigger(KeyInput.KEY_K));
        im.addMapping("L", new KeyTrigger(KeyInput.KEY_L));
        im.addMapping("M", new KeyTrigger(KeyInput.KEY_M));
        im.addMapping("N", new KeyTrigger(KeyInput.KEY_N));
        im.addMapping("O", new KeyTrigger(KeyInput.KEY_O));
        im.addMapping("P", new KeyTrigger(KeyInput.KEY_P));
        im.addMapping("Q", new KeyTrigger(KeyInput.KEY_Q));
        im.addMapping("R", new KeyTrigger(KeyInput.KEY_R));
        im.addMapping("S", new KeyTrigger(KeyInput.KEY_S));
        im.addMapping("T", new KeyTrigger(KeyInput.KEY_T));
        im.addMapping("U", new KeyTrigger(KeyInput.KEY_U));
        im.addMapping("V", new KeyTrigger(KeyInput.KEY_V));
        im.addMapping("W", new KeyTrigger(KeyInput.KEY_W));
        im.addMapping("X", new KeyTrigger(KeyInput.KEY_X));
        im.addMapping("Y", new KeyTrigger(KeyInput.KEY_Y));
        im.addMapping("Z", new KeyTrigger(KeyInput.KEY_Z));
        im.addMapping("ENTER", new KeyTrigger(KeyInput.KEY_RETURN));
        im.addMapping("BACKSPACE", new KeyTrigger(KeyInput.KEY_BACK));
    }

    private void letterPressed(String name, boolean isPressed, float tpf) {
        GameEventBroker eb = app.getGameLogic().getEventBroker();
        if (isPressed) {
            eb.notifyListeners(new LetterPressedEvent(name));
        }
    }

    private void enterPressed(String name, boolean isPressed, float tpf) {
        GameEventBroker eb = app.getGameLogic().getEventBroker();
        if (isPressed) {
            eb.notifyListeners(new LetterPressedEvent(name));
        }
    }

    private void backspacePressed(String name, boolean isPressed, float tpf) {
        GameEventBroker eb = app.getGameLogic().getEventBroker();
        if (isPressed) {
            eb.notifyListeners(new LetterPressedEvent(name));
        }
    }
}
