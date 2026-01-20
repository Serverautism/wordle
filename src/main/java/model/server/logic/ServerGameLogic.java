package model.server.logic;

import model.client.message.ClientMessage;
import model.server.Player;
import model.server.config.ServerGameConfig;
import server.network.ServerSender;

import java.util.ArrayList;
import java.util.List;

public class ServerGameLogic {
    /**
     * Logger for events and errors
     */
    public static System.Logger LOGGER = System.getLogger(ServerGameLogic.class.getName());

    /**
     * The fixed game configuration.
     */
    private final ServerGameConfig config;

    /**
     * The internal state machine governing game phases.
     */
    private ServerState state = new ServerMainState(this);

    /**
     * Used to send messages back to the clients.
     */
    private final ServerSender serversender;

    /**
     * The two players participating in this game.
     */
    private List<Player> players = new ArrayList<>(2);;

    /**
     * Constructs the game logic controller.
     *
     * @param serversender the mechanism to send messages to connected clients
     * @param config       the game configuration specifying map size and fleet
     */
    public ServerGameLogic(ServerSender serversender, ServerGameConfig config) {
        this.serversender = serversender;
        this.config = config;
        this.state.entry();
    }

    public void addPlayer (int id){
        state.addPlayer(id);
    }

    /**
     * Retrieves the game configuration.
     *
     * @return the immutable {@link ServerGameConfig}
     */
    ServerGameConfig getConfig() {
        return config;
    }

    /**
     * Returns the object responsible for dispatching messages to clients.
     *
     * @return the {@link ServerSender}
     */
    ServerSender getServerSender() {
        return serversender;
    }

    /**
     * Returns the list of players in this game.
     *
     * @return a modifiable list of up to two {@link Player} instances
     */

    List<Player> getPlayers() {
        return players;
    }

    /**
     * Finds the player associated with the given client connection ID.
     *
     * @param id the client connection identifier
     * @return the matching {@link Player}, or {@code null} if none is found
     */
    public Player getPlayerById(int id) {
        for (Player player : players) {
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

    /**
     * Receives a generic client message and forwards it to the active state.
     * <p>
     * All client messages (e.g., placements, shots) implement {@link ClientMessage}.
     * This method passes the message and sender ID to {@link ServerState}.
     * </p>
     *
     * @param msg  the incoming {@link ClientMessage}
     * @param id the connection ID of the sending client
     */

    public void receive(ClientMessage msg, int id) {
        state.receive(msg,id);
    }
}
