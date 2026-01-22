package model.server.logic;

import model.client.message.ClientMessage;
import model.client.message.LoginMessage;
import model.client.message.StartGameMessage;
import model.server.Player;
import model.server.message.ClientMessageInterpreter;
import model.server.message.ServerMessage;
import server.network.ServerSender;

/**
 * Abstract base for the various states in the server's game lifecycle.
 * <p>
 * Concrete subclasses implement state-specific behavior. This class provides
 * common utilities such as sending messages and enforcing valid operations per state.
 * </p>
 *
 * @see ServerGameLogic
 * @see ClientMessageInterpreter
 */
abstract class ServerState implements ClientMessageInterpreter {
    /**
     * The logger instance for server state transitions and actions.
     */
    static final System.Logger LOGGER = System.getLogger(ServerState.class.getName());

    /**
     * The central game logic that drives state transitions and enforces game rules.
     */
    final ServerGameLogic logic;

    /**
     * Creates a new server state tied to the given game logic controller.
     *
     * @param logic the {@link ServerGameLogic} controlling this state machine
     */
    ServerState(ServerGameLogic logic) {
        this.logic = logic;
    }

    /**
     * Sends a {@link ServerMessage} to the specified player.
     * <p>
     * Logs the outgoing message at INFO level before delegating to the {@link ServerSender}.
     * </p>
     *
     * @param player the recipient player
     * @param msg    the message to send
     */
    void send(Player player, ServerMessage msg) {
        LOGGER.log(System.Logger.Level.INFO, "sending to {0}: {1}", player, msg);
        logic.getServerSender().send(player.getId(), msg);
    }

    /**
     * Called when this state becomes active.
     * <p>
     * Subclasses may override to perform initialization, such as notifying clients
     * or scheduling timeouts. The default implementation does nothing.
     * </p>
     */
    void entry() {
        // Default implementation does nothing
    }

    /**
     * Returns the simple name of this state class, used in log messages and exceptions.
     *
     * @return the state name, typically the class’s simple name
     */
    String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Handles a client message received in this state.
     * @param msg the received message
     * @param id the player ID of the sender
     */
    public void receive(ClientMessage msg, int id) {
        final Player sender = logic.getPlayerById(id);
        if (!(msg instanceof LoginMessage) && !sender.isAuthenticated()) {
            LOGGER.log(System.Logger.Level.WARNING, "Blocked message from unauthenticated user {0}", id);
        } else {
            msg.accept(this, id);
        }
    }

    /**
     * Called when a LoginMessage is received in this state.
     * @param msg  the LoginMessage to be processed
     * @param id the connection ID from which the message was sent
     */
    public void received(LoginMessage msg, int id) {
        LOGGER.log(System.Logger.Level.ERROR, "receiving a LoginMessage not allowed in {0}", getName());
    }

    /**
     * Called when a StartGameMessage is received in this state.
     * @param msg  the StartGameMessage to be processed
     * @param id the connection ID from which the message was sent
     */
    public void received(StartGameMessage msg, int id) {
        LOGGER.log(System.Logger.Level.ERROR, "receiving a StartGameMessage not allowed in {0}", getName());
    }

    /**
     * Attempts to add a new player to the current game state.
     *
     * @param id the connection ID of the player attempting to join
     */
    void addPlayer(int id ){
        LOGGER.log(System.Logger.Level.ERROR, "addPlayer not allowed in {0}", getName());
    }

    /**
     * Notifies all players of the game setup.
     */
    private void broadcast(ServerMessage msg) {
        for (Player p : logic.getPlayers()) {
            send(p, msg);
        }
    }
}
