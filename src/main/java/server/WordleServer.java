package server;

import client.WordleApp;
import com.jme3.app.SimpleApplication;
import com.jme3.network.*;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import model.client.message.ClientMessage;
import model.client.message.LoginMessage;
import model.server.config.ServerGameConfig;
import model.server.logic.ServerGameLogic;
import model.server.message.ServerMessage;
import server.network.ReceivedMessage;
import server.network.ServerSender;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.System.Logger.Level.INFO;

public class WordleServer extends SimpleApplication implements MessageListener<HostedConnection>, ConnectionListener, ServerSender {
    private static final System.Logger LOGGER = System.getLogger(WordleServer.class.getName());

    /**
     * The configuration file for the game.
     */
    private static final File CONFIG_FILE = new File("server_config.properties");
    private final ServerGameConfig config = new ServerGameConfig();

    /**
     * The port the server is using for network communication.
     */
    private int port;

    /**
     * The network server instance used for handling client connections and communication.
     */
    private Server server;

    /**
     * The game logic instance that processes game events and client messages.
     */
    private final ServerGameLogic logic;

    /**
     * A thread-safe queue for storing incoming messages that are pending processing.
     */
    private final BlockingQueue<ReceivedMessage> pendingMessages = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        try {
            WordleServer app = new WordleServer();
            app.start(JmeContext.Type.Headless);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a BattleshipServer instance with the given configuration and port, and initializes
     * the game logic and network server.
     *
     * @throws IOException if an error occurs while starting or initializing the server.
     */
    public WordleServer() throws IOException {
        LOGGER.log(INFO, "Starting application");
        logic = new ServerGameLogic(this, config);
        loadConfig();
        makeSettings();
        startServer();
    }

    @Override
    public void simpleInitApp() {


    }

    private void startServer() throws IOException{
        LOGGER.log(System.Logger.Level.INFO, "Starting server..."); //NON-NLS
        server = Network.createServer(port);
        initializeSerializables();
        server.start();
        registerListeners();
        LOGGER.log(System.Logger.Level.INFO, "Server started: {0}", server.isRunning());
    }

    private void loadConfig() {
        LOGGER.log(INFO, "Loading configuration");
        config.readFromIfExists(CONFIG_FILE);
    }

    private void makeSettings() {
        setPauseOnLostFocus(false);
        port = config.getPort();
    }

    /**
     * Registers classes for network serialization. These classes include all the message types
     * and model objects required for client-server communication in the game.
     */
    private void initializeSerializables() {
        Serializer.registerClass(LoginMessage.class);
    }

    /**
     * Registers the message and connection listeners with the network server.
     * Sets up the server to receive  and {@link } types
     * and to handle new client connections and disconnections.
     */
    private void registerListeners() {
        server.addMessageListener(this, LoginMessage.class);
        server.addConnectionListener(this);
    }

    /**
     * @param server
     * @param hostedConnection
     */
    @Override
    public void connectionAdded(Server server, HostedConnection hostedConnection) {

    }

    /**
     * @param server
     * @param hostedConnection
     */
    @Override
    public void connectionRemoved(Server server, HostedConnection hostedConnection) {

    }

    /**
     * Callback method invoked when a message is received from a client connection.
     * If the message is an instance of {@link ClientMessage}, it is wrapped in a
     * {@link } along with the sender's ID and added to the pending messages queue.
     *
     * @param hostedConnection  the client connection from which the message was received.
     * @param message the message received from the client.
     */
    @Override
    public void messageReceived(HostedConnection hostedConnection, Message message) {
        LOGGER.log(System.Logger.Level.INFO, "message received from {0}: {1}", hostedConnection.getId(), message); //NON-NLS
        if (message instanceof ClientMessage clientMessage)
            pendingMessages.add(new ReceivedMessage(clientMessage, hostedConnection.getId()));
    }

    /**
     * Send the specified message to the client.
     *
     * @param id      the id of the client that shall receive the message
     * @param message the message
     */
    @Override
    public void send(int id, ServerMessage message) {

    }
}
