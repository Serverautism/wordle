package model.server.logic;

import model.client.message.LoginMessage;
import model.server.Player;

public class ServerMainState extends ServerState {
    /**
     * Creates a new server state tied to the given game logic controller.
     *
     * @param logic the {@link ServerGameLogic} controlling this state machine
     */
    public ServerMainState(ServerGameLogic logic) {
        super(logic);
    }

    @Override
    public void entry() {
        LOGGER.log(System.Logger.Level.INFO, "Entered State: {0}", getName());
    }

    /**
     * Registers a new player in the game.
     *
     * @param id the connection ID of the joining player
     */
    @Override
    void addPlayer(int id) {
        final int playerNumber = logic.getPlayers().size() + 1;
        final Player player = new Player("player " + playerNumber, id);
        LOGGER.log(System.Logger.Level.INFO, "adding {0}");
        logic.getPlayers().add(player);
    }

    /**
     * Called when a LoginMessage is received in this state.
     * @param msg  the LoginMessage to be processed
     * @param id the connection ID from which the message was sent
     */
    @Override
    public void received(LoginMessage msg, int id) {
        LOGGER.log(System.Logger.Level.INFO, "Client {0} is trying to authenticate", id);
        final Player sender = logic.getPlayerById(id);
        if (sender != null) {
            sender.authenticate(logic.getConfig().getUserFolder(), msg.getName(), msg.getPassword());
            if (sender.isAuthenticated()) {
                LOGGER.log(System.Logger.Level.INFO, "Client {0} is authenticated successfully with name {1}", id, sender.getName());
            } else {
                LOGGER.log(System.Logger.Level.WARNING, "Client {0} failed authentication", id);
                //TODO: client raus werfen
            }
        }
    }
}
