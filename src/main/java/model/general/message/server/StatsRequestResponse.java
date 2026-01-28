package model.general.message.server;

import com.jme3.network.serializing.Serializable;
import model.general.message.client.ServerMessageInterpreter;
import model.server.Player;

@Serializable
public class StatsRequestResponse extends ServerMessage {
    /**
     * The players public alias
     */
    private String alias;

    /**
     * The players last login day
     */
    private long lastPlayDate;

    /**
     * The players overall score
     */
    private int score;

    /**
     * The players current daily wordle streak
     */
    private int streak;

    /**
     * The players longest ever wordle streak
     */
    private int maxStreak;

    /**
     * The total amount of wordles solved
     */
    private int wordlesSolved;

    /**
     * The total amount of wordles lost
     */
    private int wordlesLost;

    /**
     * The distribution of guesses needed to solve wordles
     */
    private int[] guessDistribution;

    /**
     * No-argument constructor for serialization purposes
     */
    private StatsRequestResponse() {}

    /**
     * Constructs a new StatsRequestResponse containing information about the give Player
     *
     * @param player the provided player
     */
    public StatsRequestResponse(Player player) {
        alias = player.getAlias();
        lastPlayDate = player.getLastPlayDate();
        score = player.getScore();
        streak = player.getStreak();
        maxStreak = player.getMaxStreak();
        wordlesSolved = player.getWordlesSolved();
        wordlesLost = player.getWordlesLost();
        guessDistribution = player.getGuessDistribution();
    }

    /**
     * Accepts a visitor for processing this message.
     *
     * @param interpreter the visitor to be used for processing
     */
    @Override
    public void accept(ServerMessageInterpreter interpreter) {
        interpreter.received(this);
    }

    public String getAlias() {
        return alias;
    }

    public long getLastPlayDate() {
        return lastPlayDate;
    }

    public int getScore() {
        return score;
    }

    public int getStreak() {
        return streak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public int getWordlesSolved() {
        return wordlesSolved;
    }

    public int getWordlesLost() {
        return wordlesLost;
    }

    public int[] getGuessDistribution() {
        return guessDistribution;
    }
}
