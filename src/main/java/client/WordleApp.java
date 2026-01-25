package client;

import client.network.ClientNetworkSupport;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.font.BitmapFont;
import com.jme3.system.AppSettings;
import model.client.Feature;
import model.client.config.ClientGameConfig;
import model.client.logic.ClientGameLogic;
import model.client.message.ClientMessage;
import model.client.message.LoginMessage;
import model.client.message.ServerMessageReceiver;
import model.client.notification.GameEventBroker;
import model.server.message.ServerMessage;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WordleApp extends SimpleApplication implements ServerMessageReceiver {
    /**
     * Logger for events and errors
     */
    private static final System.Logger LOGGER = System.getLogger(WordleApp.class.getName());

    /**
     * The configuration file for the game.
     */
    private static final File CONFIG_FILE = new File("client_config.properties");
    private final ClientGameConfig config = new ClientGameConfig();

    /**
     * Handles logic and controls client states.
     */
    private ClientGameLogic logic;

    /**
     * Manages the client network connection.
     */
    private ClientNetworkSupport network;

    /**
     * Executor service for handling asynchronous tasks.
     */
    private ExecutorService executor;

    private Future<?> connectionProcess;

    public static void main(String[] args) {
        WordleApp app = new WordleApp();
        app.start();
    }

    public WordleApp() {
        LOGGER.log(System.Logger.Level.INFO, "Starting application, reading configuration");
        config.readFromIfExists(CONFIG_FILE);
        setShowSettings(config.getShowSettings());
        setSettings(makeSettings());
    }

    /**
     * Creates an {@link AppSettings} object configured from the current configuration.
     *
     * @return the {@link AppSettings} instance.
     */
    private AppSettings makeSettings() {
        LOGGER.log(System.Logger.Level.INFO, "Applying settings");
        final AppSettings settings = new AppSettings(true);
        settings.setTitle("Wordle");
        settings.setResolution(config.getResolutionWidth(), config.getResolutionHeight());
        settings.setFullscreen(config.isFullScreen());
        //settings.setUseRetinaFrameBuffer(config.isUseRetinaFrameBuffer());
        settings.setGammaCorrection(config.isUseGammaCorrection());
        return settings;
    }

    /**
     * Returns the current {@link ClientGameLogic} instance.
     *
     * @return the game logic.
     */
    public ClientGameLogic getGameLogic() {
        return logic;
    }

    /**
     * Returns the current configuration settings.
     *
     * @return the {@link ClientGameConfig} instance.
     */
    public ClientGameConfig getConfig() {
        return config;
    }

    /**
     * Returns the {@link ClientNetworkSupport} instance used for network communication.
     *
     * @return the network instance.
     */
    ClientNetworkSupport getNetwork() {
        return network;
    }

    @Override
    public void simpleInitApp() {
        LOGGER.log(System.Logger.Level.INFO, "Initializing application");
        setPauseOnLostFocus(false);
        inputManager.deleteMapping(INPUT_MAPPING_EXIT);
        inputManager.setCursorVisible(false);

        //inputManager.addMapping(WKEY,  new KeyTrigger(KeyInput.KEY_W));

        setupStates();
    }

    /**
     * Initializes and attaches the app states.
     * This method attaches game-related states, disables the flyCam,
     * and optionally attaches statistics and debugging states if enabled in the configuration.
     */
    private void setupStates() {
        if (config.isShowStatistics()) {
            final BitmapFont normalFont = assetManager.loadFont("Interface\\Fonts\\Default.png");
            final StatsAppState stats = new StatsAppState(guiNode, normalFont);
            stateManager.attach(stats);
        }
        flyCam.setEnabled(false);
        stateManager.detach(stateManager.getState(StatsAppState.class));
        stateManager.detach(stateManager.getState(DebugKeysAppState.class));
        stateManager.attachAll(new InputState());
    }

    /**
     * Updates the application every frame.
     *
     * @param tpf the time per frame in seconds.
     */
    @Override
    public void simpleUpdate(float tpf) {
        super.simpleUpdate(tpf);
        initializeGame();
        authenticate();
        logic.update(tpf);
        checkStates();
    }

    /**
     * Enables or disables relevant game states.
     */
    private void checkStates() {
        final Set<Feature> features = logic.getFeatures();
        //editorState.setEnabled(features.contains(Feature.EDITOR));
    }

    /**
     * Initializes the game components.
     * This method is invoked during the first frame update. It creates the network
     * connection, instantiates the client game logic, and adds event listeners.
     * It also presents the network connection dialog.
     */
    private void initializeGame() {
        if (logic != null) return;
        LOGGER.log(System.Logger.Level.INFO, "Initializing game");
        final GameEventBroker eventBroker = new GameEventBroker();
        network = new ClientNetworkSupport(this, eventBroker);
        logic = new ClientGameLogic(network, eventBroker, getConfig());
        // eventBroker.addListener(soundState);
        connectionProcess = getExecutor().submit(() -> {
            try {
                getNetwork().connect(config.getHostname(), config.getPort());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void authenticate() {
        if (connectionProcess == null) return;
        if (connectionProcess.isDone()) {
            LOGGER.log(System.Logger.Level.INFO, "Sending authentication");
            //logic.send(new LoginMessage("josi", "1201"));
            connectionProcess = null;
        }
    }

    /**
     * Handles client requests to close the application.
     * This method is a no-op when triggered by in-game events (it can be extended if needed).
     *
     * @param esc true if the closing request is due to the Escape key.
     */
    @Override
    public void requestClose(boolean esc) { /* do nothing */ }

    /**
     * Immediately closes the application by disconnecting from the server and stopping the app.
     */
    public void close() {
        network.disconnect();
        stop();
    }

    /**
     * Returns the {@link ExecutorService} used for running asynchronous tasks.
     *
     * @return the executor service.
     */
    public ExecutorService getExecutor() {
        if (executor == null)
            executor = Executors.newCachedThreadPool();
        return executor;
    }

    /**
     * Stops the application.
     * This method shuts down the executor service and then stops the application.
     *
     * @param waitFor true if the stop process should wait for termination.
     */
    @Override
    public void stop(boolean waitFor) {
        if (executor != null) executor.shutdownNow();
        super.stop(waitFor);
    }

    /**
     * Processes a server message received from the network.
     *
     * @param msg the server message.
     */
    @Override
    public void receive(ServerMessage msg) {
        enqueue(() -> logic.receive(msg));
    }
}
