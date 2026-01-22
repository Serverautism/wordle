package model.server;

import java.io.File;
import java.io.IOException;

/**
 * Represents a game participant on the server side, holding a unique connection ID and a display name.
 */
public class Player {
    /**
     * The display name of the player.
     */
    private String name;

    /**
     * The day the player last played the game
     */
    private long lastPlayDate;

    /**
     * The players overall score
     */
    private int score;

    /**
     * The unique connection ID of the player.
     */
    private final int id;

    /**
     * Indicates if the player hab a valid login
     */
    private boolean authenticated = false;

    /**
     * Indicates if the player is currently playing a game
     */
    private boolean gameActive = false;

    /**
     * The amount of guesses made in the current game
     */
    private int guessesMade;

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
            if (password.equals(dto.getPassword())) {
                setAuthenticated(true);
                loadStats(dto);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadStats(PlayerAuthDTO dto) {
        name = dto.getName();
        score = dto.getScore();
        lastPlayDate = dto.getLastPlayDate();
    }

    public void startGame(String answer) {
        gameActive = true;
        guessesMade = 0;
        currentAnswer = answer;
    }

    /**
     * Gets the display name of the player.
     * @return the name
     */
    public String getName() {
        return name;
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

    public long getLastPlayDate() {
        return lastPlayDate;
    }

    public void setLastPlayDate(long day) {
        //TODO: streak berechnen
        lastPlayDate = day;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public int getGuessesMade() {
        return guessesMade;
    }

    public String getCurrentAnswer() {
        return currentAnswer;
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
