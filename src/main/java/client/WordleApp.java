package client;

import client.network.ClientNetworkSupport;
import com.jme3.app.SimpleApplication;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class WordleApp extends SimpleApplication{
    /**
     * Logger for events and errors
     */
    private static final System.Logger LOGGER = System.getLogger(WordleApp.class.getName());

    /**
     * The configuration file for the game.
     */
    private static final File CONFIG_FILE = new File("config.properties");

    /**
     * Manages the client network connection.
     */
    private ClientNetworkSupport network;

    /**
     * Executor service for handling asynchronous tasks.
     */
    private ExecutorService executor;

    public static void main(String[] args) {
        WordleApp app = new WordleApp();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
    }
}
