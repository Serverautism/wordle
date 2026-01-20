package client.network;

import com.jme3.network.*;
import model.client.message.ClientMessage;
import model.client.message.ServerMessageReceiver;
import model.client.notification.GameEventBroker;
import model.server.message.ServerMessage;

import java.io.IOException;

/**
 * Handles network communication for game clients.
 */
public class ClientNetworkSupport implements MessageListener<Client>, ClientStateListener, ClientSender {
    private static final System.Logger LOGGER = System.getLogger(ClientNetworkSupport.class.getName());

    /**
     * Hostname for connecting to a local server.
     */
    public static final String LOCALHOST = "localhost"; //NON-NLS

    /**
     * The receiver that handles messages sent from the server.
     */
    private final ServerMessageReceiver receiver;

    /**
     * The client used to communicate with the server.
     */
    private Client client;

    /**
     * The broker for distributing game and model events to registered listeners.
     */
    private final GameEventBroker eventBroker;

    /**
     * Constructs a {@code ClientNetworkSupport} instance with the specified message receiver.
     *
     * @param receiver    the receiver to handle messages coming from the server.
     * @param eventBroker the event broker used for notifying listeners.
     */
    public ClientNetworkSupport(ServerMessageReceiver receiver, GameEventBroker eventBroker) {
        this.receiver = receiver;
        this.eventBroker = eventBroker;
    }

    /**
     * Checks whether the client is currently connected to the server.
     *
     * @return {@code true} if the client is not {@code null} and is connected to the server,
     * {@code false} otherwise.
     */
    public boolean isConnected() {
        return client != null && client.isConnected();
    }

    /**
     * Closes the client connection.
     * <p>
     * If the client is already {@code null}, no action is taken. Otherwise,
     * the connection is closed, and the client reference is set to {@code null}.
     * </p>
     */
    public void disconnect() {
        if (client == null) return;
        client.close();
        client = null;
        LOGGER.log(System.Logger.Level.INFO, "client closed"); //NON-NLS
    }

    /**
     * Connects to the server using the specified host and port.
     * <p>
     * This method creates a new client connection, starts the client, and registers
     * both message and state listeners. If a client connection already exists, an
     * {@link IllegalStateException} is thrown.
     * </p>
     *
     * @param host the server hostname or IP address.
     * @param port the port number on which the server is listening.
     * @throws IOException           if an I/O error occurs during the connection process.
     * @throws IllegalStateException if a connection already exists.
     */
    public void connect(String host, int port) throws IOException {
        if (client != null)
            throw new IllegalStateException("trying to join a game again");
        client = Network.connectToServer(host, port);
        client.start();
        client.addMessageListener(this);
        client.addClientStateListener(this);
    }

    /**
     * Callback invoked when a message is received from the server.
     * <p>
     * If the incoming message is an instance of {@link ServerMessage}, it is forwarded
     * to the registered {@code ServerMessageReceiver} for further processing.
     * </p>
     *
     * @param client  the client that received the message.
     * @param message the {@link Message} received from the server.
     */
    @Override
    public void messageReceived(Client client, Message message) {
        LOGGER.log(System.Logger.Level.INFO, "message received from server: {0}", message); //NON-NLS
        if (message instanceof ServerMessage serverMessage)
            receiver.receive(serverMessage);
    }

    /**
     * Callback invoked when the client successfully connects to the server.
     *
     * @param client the client instance that connected.
     */
    @Override
    public void clientConnected(Client client) {
        LOGGER.log(System.Logger.Level.INFO, "Client connected: {0}", client); //NON-NLS
    }

    /**
     * Callback invoked when the client is disconnected from the server.
     * <p>
     * This method logs the disconnection details and verifies that the disconnected
     * client is the same as the one managed by this instance. It then resets the
     * client reference and notifies the client of the lost connection.
     * </p>
     *
     * @param disconnected   the client instance that was disconnected.
     * @param disconnectInfo additional information regarding the disconnection.
     * @throws IllegalArgumentException if the disconnected client is not the managed client.
     */
    @Override
    public void clientDisconnected(Client disconnected, DisconnectInfo disconnectInfo) {
        LOGGER.log(System.Logger.Level.INFO, "Client {0} disconnected: {1}", disconnected, disconnectInfo); //NON-NLS
        if (client != disconnected)
            throw new IllegalArgumentException("parameter value must be the client");
        LOGGER.log(System.Logger.Level.INFO, "Client still connected: {0}", disconnected.isConnected()); //NON-NLS
        client = null;
        error("lost.connection.to.server");
    }

    /**
     * Sends the specified client message to the server.
     * <p>
     * If there is no active client connection, an error message is displayed via the application.
     * </p>
     *
     * @param message the {@link ClientMessage} to be sent to the server.
     */
    @Override
    public void send(ClientMessage message) {
        LOGGER.log(System.Logger.Level.INFO, "sending {0}", message); //NON-NLS
        if (client == null)
            error("lost.connection.to.server");
        else
            client.send(message);
    }

    /**
     * Sends an error message to the client.
     * @param text the key for the error message to be displayed.
     */
    private void error(String text) {
        LOGGER.log(System.Logger.Level.ERROR, "error {0}", text);
    }
}
