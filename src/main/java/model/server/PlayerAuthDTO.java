package model.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerAuthDTO {
    static final System.Logger LOGGER = System.getLogger(PlayerAuthDTO.class.getName());

    private String name;
    private String alias;
    private String password;
    private long lastPlayDate;
    private int score;
    private int streak;
    private int maxStreak;
    private int wordlesSolved;
    private int wordlesLost;
    private int[] guessDistribution;

    public void setStatsTo(Player player) {
        name = player.getName();
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
     * Saves this {@code MapDTO} as a JSON file.
     *
     * @param file the file to write to
     * @throws IOException if an I/O error occurs
     */
    public void saveTo(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            final Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final String json = gson.toJson(this);
            LOGGER.log(System.Logger.Level.DEBUG, "JSON of player: {0}", json);
            writer.write(json);
            LOGGER.log(System.Logger.Level.INFO, "JSON written to {0}", file.getAbsolutePath());
        }
    }

    /**
     * Loads a {@code MapDTO} from a JSON file.
     *
     * @param file the file to read from
     * @return the loaded {@code MapDTO}
     * @throws IOException if reading or parsing fails
     */
    public static PlayerAuthDTO loadFrom(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            final Gson gson = new Gson();
            return gson.fromJson(reader, PlayerAuthDTO.class);
        } catch (JsonParseException e) {
            throw new IOException(e.getLocalizedMessage());
        }
    }


    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean checkPassword(String pwd) {
        return password.equals(pwd);
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
