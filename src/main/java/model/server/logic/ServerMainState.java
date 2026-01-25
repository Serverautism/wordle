package model.server.logic;

import model.client.message.GuessMessage;
import model.client.message.LoginMessage;
import model.client.message.StartGameMessage;
import model.server.Player;
import model.server.message.GuessResponse;
import model.server.message.LoginResponse;
import model.server.message.StartGameResponse;

import java.util.ArrayList;

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
                send(sender, new LoginResponse());
            } else {
                LOGGER.log(System.Logger.Level.WARNING, "Client {0} failed authentication", id);
                //TODO: client raus werfen
            }
        }
    }

    /**
     * Called when a StartGameMessage is received in this state.
     * @param msg  the StartGameMessage to be processed
     * @param id the connection ID from which the message was sent
     */
    @Override
    public void received(StartGameMessage msg, int id) {
        final Player sender = logic.getPlayerById(id);
        LOGGER.log(System.Logger.Level.INFO, "Client {0} with name {1} is trying to start a game", id, sender.getName());
        if (sender.isGameActive()) {
            LOGGER.log(System.Logger.Level.WARNING, "Client {0} with name {1} is already in an active game", id, sender.getName());
            return;
        }
        if (!(sender.getLastPlayDate() == logic.getWordleEngine().getCurrentPlayDay())) {
            LOGGER.log(System.Logger.Level.WARNING, "Client {0} with name {1} started first game of the day", id, sender.getName());
            sender.setLastPlayDate(logic.getWordleEngine().getCurrentPlayDay());
            sender.startGame(logic.getWordleEngine().getCurrentWord(), 6);
            sender.setPointsToGain(logic.getConfig().getPointsDaily());
        } else {
            LOGGER.log(System.Logger.Level.WARNING, "Client {0} with name {1} started game with random word", id, sender.getName());
            sender.startGame(logic.getWordleEngine().getRandomWord(), 6);
            sender.setPointsToGain(logic.getConfig().getPointsRandom());
        }
        send(sender, new StartGameResponse(sender.getCurrentAnswer().length()));
    }

    /**
     *
     *
     * @param msg  the GuessMessage to be processed
     * @param id the connection ID from which the message was sent
     */
    @Override
    public void received(GuessMessage msg, int id) {
        final Player sender = logic.getPlayerById(id);
        if (!sender.isGameActive()) {
            LOGGER.log(System.Logger.Level.WARNING, "Client {0} with name {1} has not started a game yet", id, sender.getName());
            return;
        }
        if (sender.canSubmitGuess() && logic.getWordleEngine().isValidWord(msg.getGuess())) {
            LOGGER.log(System.Logger.Level.INFO, "Client {0} with name {1}: accepted guess {2} (answer is {3})", id, sender.getName(), msg.getGuess(), sender.getCurrentAnswer());
            sender.submitGuess(msg.getGuess());
            send(sender, new GuessResponse(true, logic.getWordleEngine().evaluateGuess(msg.getGuess(), sender.getCurrentAnswer())));
            if (msg.getGuess().equals(sender.getCurrentAnswer())) {
                LOGGER.log(System.Logger.Level.INFO, "Client {0} with name {1}: guessed the correct answer", id, sender.getName());
                sender.endGame();
            }
        } else {
            LOGGER.log(System.Logger.Level.WARNING, "Client {0} with name {1}: rejected guess {2} (answer is {3})", id, sender.getName(), msg.getGuess(), sender.getCurrentAnswer());
            send(sender, new GuessResponse(false, new ArrayList<>()));
        }
        LOGGER.log(System.Logger.Level.INFO, "Client {0} with name {1} has {2} guesses remaining", id, sender.getName(), sender.getRemainingGuesses());
    }
}
