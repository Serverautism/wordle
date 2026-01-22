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
    private String password;
    private int score;
    private long lastPlayDate;

    /**
     * Creates a PlayerAuthDTO from a Player instance
     */
    public PlayerAuthDTO(Player player) {

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
            LOGGER.log(System.Logger.Level.DEBUG, "JSON of map: {0}", json);
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

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public long getLastPlayDate() {
        return lastPlayDate;
    }
}
