package model.client.logic;

import client.network.ClientSender;
import model.client.Feature;
import model.client.config.ClientGameConfig;
import model.client.message.ClientMessage;
import model.client.message.ServerMessageReceiver;
import model.client.notification.GameEventBroker;
import model.server.message.ServerMessage;

import java.util.Set;

/**
 * Central client-side controller for the game.
 * <p>
 * {@code ClientGameLogic} coordinates the interaction between the current
 * {@link ClientState}, the server, and the local game model. It processes
 * incoming {@link ServerMessage}s, forwards user input to the active state,
 * manages the map and configuration, and triggers events such as sounds or
 * animation notifications via the {@link GameEventBroker}.
 * </p>
 * <p>
 * The class acts as a state machine driver: most actions (clicks, updates,
 * turn changes) are delegated to the current {@link ClientState}, which
 * encapsulates the concrete behavior for the active phase of the game.
 * </p>
 */

public class ClientGameLogic implements ServerMessageReceiver {
    /**
     * Logger for this class
     */
    public static System.Logger LOGGER = System.getLogger(ClientGameLogic.class.getName());

    /**
     * The current game configuration.
     */
    private ClientGameConfig gameConfig;

    /**
     * The event broker responsible for dispatching game-related events.
     */
    private GameEventBroker eventBroker;

    /**
     * The network sender used to transmit client messages.
     */
    private ClientSender clientSender;

    /**
     * The current game state.
     */
    private ClientState state = new InitialState(this);

    /**
     * Creates a new instance of the client-side game logic, managing the
     * communication between the client, the server, and the game state machine.
     *
     * @param sender      the network sender used to transmit client messages
     * @param eventBroker broker responsible for dispatching game-related events
     * @param config the game config containing game information
     */
    public ClientGameLogic(ClientSender sender, GameEventBroker eventBroker, ClientGameConfig config){
        this.clientSender = sender;
        this.eventBroker = eventBroker;
        this.gameConfig = config;
        state.entry();
    }

    /**
     * Updates the client logic once per frame.
     *
     * @param delta the time passed since the last update
     */
    public void update(float delta) {
        state.update(delta);
    }

    /**
     * @return the event broker responsible for event dispatching
     */
    public GameEventBroker getBroker() {
        return eventBroker;
    }

    /**
     * @return the current state of the client state machine
     */
    public ClientState getState() {
        return state;
    }

    /**
     * Returns the GameEventBroker.
     * @return the GameEventBroker
     */
    public GameEventBroker getEventBroker() {
        return eventBroker;
    }

    /**
     * @return the current game configuration
     */
    public ClientGameConfig getGameConfig() {
        return gameConfig;
    }

    /**
     * @return all features supported by the current client state
     */
    public Set<Feature> getFeatures() {
        return state.getFeatures();
    }

    /**
     * Switches the client to a new state and triggers the state's entry behavior.
     *
     * @param newState the new client state to transition into
     */
    public void setState(ClientState newState) {
        LOGGER.log(System.Logger.Level.INFO, "state transition {0} --> {1}", state.getName(), newState.getName()); //NON-NLS
        state = newState;
        state.entry();
    }

    /**
     * Sends a client-created message to the server.
     *
     * @param msg the message to transmit
     */
    public void send(ClientMessage msg) {
        clientSender.send(msg);
    }

    /**
     * Handles incoming messages from the server by delegating them
     * to the current client state.
     *
     * @param msg the received server message
     */
    @Override
    public void receive(ServerMessage msg) {
        state.receive(msg);
    }
}
