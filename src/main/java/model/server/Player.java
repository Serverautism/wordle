package model.server;

import model.general.dto.StatsProvider;

import java.io.File;
import java.io.IOException;

/**
 * Represents a game participant on the server side, holding a unique connection ID and a display name.
 */
public class Player implements StatsProvider {
    static final System.Logger LOGGER = System.getLogger(Player.class.getName());

    /**
     * The players username
     */
    private String name;

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
     * Points the player could win when solving this wordle
     */
    private int pointsToGain = 1;

    /**
     * The unique connection ID of the player.
     */
    private final int id;

    /**
     * Indicates if the player has a valid login
     */
    private boolean authenticated = false;

    /**
     * Indicates if the player is currently playing a game
     */
    private boolean gameActive = false;

    /**
     * Indicates if the currently played wordle is the daily wordle
     */
    private boolean dailyWordle = false;

    /**
     * The amount of guesses made in the current game
     */
    private int guessesMade;

    /**
     * The max amount of guesses for this game
     */
    private int maxGuessAmount;

    /**
     * The answer to the game the player is currently playing
     */
    private String currentAnswer;

    /**
     * Constructor for the Player class.
     * @param name the display name of the player
     * @param id the unique connection ID of the player
     */
    public Player(final String name, final int id) {
        this.name = name;
        this.id = id;
    }

    public void authenticate(String location, String name, String password) {
        final String path = location + name + ".json";
        try {
            final PlayerAuthDTO dto = PlayerAuthDTO.loadFrom(new File(path));
            if (dto.checkPassword(password)) {
                setAuthenticated(true);
                loadStats(dto);
            }
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Could not load player data for player name: {0}", name);
        }
    }

    private void loadStats(PlayerAuthDTO dto) {
        name = dto.getName();
        alias = dto.getAlias();
        lastPlayDate = dto.getLastPlayDate();
        score = dto.getScore();
        streak = dto.getStreak();
        maxStreak = dto.getMaxStreak();
        wordlesSolved = dto.getWordlesSolved();
        wordlesLost = dto.getWordlesLost();
        guessDistribution = dto.getGuessDistribution();
    }

    public void saveStats(String location) {
        final String path = location + name + ".json";
        try {
            PlayerAuthDTO dto = PlayerAuthDTO.loadFrom(new File(path));
            dto.setStatsTo(this);
            dto.saveTo(new File(path));
        } catch (IOException e) {
            LOGGER.log(System.Logger.Level.ERROR, "Could not save player data for player name: {0}", name);
        }
    }

    public void startGame(String answer, int maxGuesses) {
        gameActive = true;
        guessesMade = 0;
        currentAnswer = answer;
        maxGuessAmount = maxGuesses;
    }

    public void endGame(boolean won) {
        if (won) {
            score += pointsToGain;
            wordlesSolved += 1;
            guessDistribution[guessesMade - 1] += 1;
        } else {
            if (!dailyWordle && score > 0) {
                score -= 1;
                wordlesLost += 1;
            }
        }

        if (dailyWordle) {
            if (won) {
                streak += 1;
                if (maxStreak < streak) {
                    maxStreak = streak;
                }
            } else {
                streak = 0;
            }
        }

        gameActive = false;
    }

    /**
     * Gets the display name of the player.
     * @return the name
     */
    public String getName() {
        return name;
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

    /**
     * Gets the unique connection ID of the player.
     * @return the id
     */
    public int getId() {
        return id;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    private void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public void setLastPlayDate(long day) {
        lastPlayDate = day;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public int getGuessesMade() {
        return guessesMade;
    }

    public int getRemainingGuesses() {
        return maxGuessAmount - guessesMade;
    }

    public boolean canSubmitGuess() {
        return guessesMade < maxGuessAmount;
    }

    public void submitGuess(String guess) {
        guessesMade += 1;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
    }

    public void setDailyOrRandom(boolean daily, int pointsToGain) {
        dailyWordle = daily;
        this.pointsToGain = pointsToGain;
    }

    /**
     * Returns a string representation of the player.
     * @return the string representation
     */
    @Override
    public String toString() {
        return String.format("Player [name=%s, id=%d]", name, id);
    }
}
