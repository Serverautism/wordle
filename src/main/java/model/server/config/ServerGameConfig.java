package model.server.config;

import model.general.config.Config;

public class ServerGameConfig extends Config {
    /**
     * The default port number for the game server.
     */
    @Property("port")
    private int port = 1234;

    /**
     * Path to the file representing the wordlist.
     */
    @Property("words.list")
    private String wordListPath;

    /**
     * Path to the folder containing the user files.
     */
    @Property("user.folder")
    private String userFolder;

    public String getWordListPath() {
        return wordListPath;
    }

    public String getUserFolder() {
        return userFolder;
    }

    public int getPort() {
        return port;
    }
}
