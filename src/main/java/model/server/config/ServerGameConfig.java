package model.server.config;

import model.general.config.Config;

public class ServerGameConfig extends Config {
    /**
     * The default port number for the game server.
     */
    @Property("port")
    private int port = 1234;

    /**
     * Path to the file representing the wordlist for all allowed answers.
     */
    @Property("words.answers")
    private String answerListPath;

    /**
     * Path to the file representing the wordlist for all allowed guesses.
     */
    @Property("words.guesses")
    private String guessListPath;

    /**
     * Path to the folder containing the user files.
     */
    @Property("user.folder")
    private String userFolder;

    public String getAnswerListPath() {
        return answerListPath;
    }

    public String getGuessListPath() {
        return guessListPath;
    }

    public String getUserFolder() {
        return userFolder;
    }

    public int getPort() {
        return port;
    }
}
