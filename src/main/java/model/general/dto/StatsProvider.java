package model.general.dto;

public interface StatsProvider {
    String getAlias();
    long getLastPlayDate();
    int getScore();
    int getStreak();
    int getMaxStreak();
    int getWordlesSolved();
    int getWordlesLost();
    int[] getGuessDistribution();
}
