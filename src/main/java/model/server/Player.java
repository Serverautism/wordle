package model.server;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a game participant on the server side, holding a unique connection ID and a display name.
 */
public class Player {
    /**
     * The display name of the player.
     */
    private String name;

    /**
     * The players overall score
     */
    private int score;

    /**
     * The unique connection ID of the player.
     */
    private final int id;

    private boolean authenticated = false;

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

    /**
     * Returns a string representation of the player.
     * @return the string representation
     */
    @Override
    public String toString() {return String.format("Player [name=%s, id=%d]", name, id);}
}
