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
     * The amount of points gaines for solving the daily wordle.
     */
    @Property("points.daily")
    private int pointsDaily = 10;

    /**
     * The amount of points gaines for solving a random wordle.
     */
    @Property("points.random")
    private int pointsRandom = 1;

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

    public int getPointsDaily() {
        return pointsDaily;
    }

    public int getPointsRandom() {
        return pointsRandom;
    }
}
