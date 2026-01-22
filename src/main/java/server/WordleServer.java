package server;

import client.WordleApp;
import com.jme3.app.SimpleApplication;
import com.jme3.network.*;
import com.jme3.network.serializing.Serializer;
import com.jme3.system.JmeContext;
import model.client.message.ClientMessage;
import model.client.message.LoginMessage;
import model.client.message.StartGameMessage;
import model.server.Player;
import model.server.config.ServerGameConfig;
import model.server.logic.ServerGameLogic;
import model.server.message.LoginResponse;
import model.server.message.ServerMessage;
import model.server.message.StartGameResponse;
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
        loadConfig();
        makeSettings();
        startServer();
        logic = new ServerGameLogic(this, config);
    }

    @Override
    public void simpleInitApp() {


    }

    /**
     * The main server loop that continuously processes pending messages until a stop request is received.
     */
    @Override
    public void simpleUpdate(float tpf) {
        processNextMessage();
        logic.update(tpf);
    }

    /**
     * Processes the next pending message from the queue and delegates it to the game logic for handling.
     * If interrupted while waiting, logs the interruption and re-interrupts the thread.
     */
    private void processNextMessage() {
        try {
            pendingMessages.take().process(logic);
        }
        catch (InterruptedException ex) {
            LOGGER.log(System.Logger.Level.INFO, "Interrupted while waiting for messages");
            // Restore the interrupt flag and continue
            Thread.currentThread().interrupt();
        }
    }

    private void startServer() throws IOException{
        LOGGER.log(System.Logger.Level.INFO, "Starting server...");
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
        Serializer.registerClass(StartGameMessage.class);

        Serializer.registerClass(LoginResponse.class);
        Serializer.registerClass(StartGameResponse.class);
    }

    /**
     * Registers the message and connection listeners with the network server.
     * Sets up the server to receive  and {@link } types
     * and to handle new client connections and disconnections.
     */
    private void registerListeners() {
        server.addMessageListener(this, LoginMessage.class);
        server.addMessageListener(this, StartGameMessage.class);
        server.addConnectionListener(this);
    }

    /**
     * @param server
     * @param hostedConnection
     */
    @Override
    public void connectionAdded(Server server, HostedConnection hostedConnection) {
        LOGGER.log(System.Logger.Level.INFO, "new connection {0}", hostedConnection);
        logic.addPlayer(hostedConnection.getId());
    }

    /**
     * @param server
     * @param hostedConnection
     */
    @Override
    public void connectionRemoved(Server server, HostedConnection hostedConnection) {
        LOGGER.log(System.Logger.Level.INFO, "connection closed: {0}", hostedConnection);
        final Player player = logic.getPlayerById(hostedConnection.getId());
        if (player == null)
            LOGGER.log(System.Logger.Level.INFO, "closed connection does not belong to an active player");
        else {
            LOGGER.log(System.Logger.Level.INFO, "closed connection belongs to {0}", player);
            stop();
        }
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
        LOGGER.log(System.Logger.Level.INFO, "message received from {0}: {1}", hostedConnection.getId(), message);
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
        if (server == null || !server.isRunning()) {
            LOGGER.log(System.Logger.Level.ERROR, "no server running when trying to send {0}", message);
            return;
        }
        final HostedConnection connection = server.getConnection(id);
        if (connection != null)
            connection.send(message);
        else
            LOGGER.log(System.Logger.Level.ERROR, "there is no connection with id={0}", id);
    }

    /**
     * Stops the server by closing all active client connections and setting a flag
     * to exit the processing loop.
     */
    private void stopServer() {
        LOGGER.log(System.Logger.Level.INFO, "close request");
        if (server != null)
            for (HostedConnection client : server.getConnections())
                if (client != null)
                    client.close("Game over");
        stop();
    }
}
