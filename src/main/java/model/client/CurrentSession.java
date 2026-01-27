package model.client;

import client.InputState;
import model.general.config.CharacterPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentSession {
    /**
     * Logger for events and errors
     */
    private static final System.Logger LOGGER = System.getLogger(CurrentSession.class.getName());

    private final int maxGuessAmount;
    private final int answerLength;

    private String unsubmittedGuess = "";

    private List<String> submittedGuesses = new ArrayList<>();
    private List<List<CharacterPosition>> positions = new ArrayList<>();

    private Map<Character, CharacterPosition> letterState = new HashMap<>();

    /**
     * Creates a new CurrentSession, containing information about the wordle
     * that is currently being solved
     */
    public CurrentSession(int answerLength, int allowedGuesses) {
        maxGuessAmount = allowedGuesses;
        this.answerLength = answerLength;
    }

    /**
     * Returns if the current guess is long enough
     *
     * @return true if guess is as long as the answer
     */
    public boolean isCurrentGuessValid() {
        return unsubmittedGuess.length() == answerLength && remainingGuesses() > 0;
    }

    /**
     * Returns the word that is currently inputted
     *
     * @return word as String
     */
    public String getUnsubmittedGuess() {
        return unsubmittedGuess;
    }

    /**
     * Return List of words that have been submitted
     *
     * @return list of words as List of Strings
     */
    public List<String> getSubmittedGuesses() {
        return submittedGuesses;
    }

    /**
     * Returns two-dimensional List containing information about the positioning of characters
     *
     * @return two-dimensional List of CharacterPosition
     */
    public List<List<CharacterPosition>> getPositions() {
        return positions;
    }

    /**
     * Submits and clears the current guess and adds positions information
     */
    public void submitGuess(List<CharacterPosition> positions) {
        if (canSubmitGuess()) {
            safeLetterState(unsubmittedGuess, positions);
            submittedGuesses.add(unsubmittedGuess);
            unsubmittedGuess = "";
            this.positions.add(positions);
        }
    }

    /**
     * Updates the letter states based on the guess and its evaluation
     *
     * @param guess the guess
     * @param states the guesses rating
     */
    private void safeLetterState(String guess, List<CharacterPosition> states) {
        for (int i = 0; i < guess.length(); i++) {
            Character c = guess.charAt(i);
            if (letterState.containsKey(c)) {
                if (letterState.get(c).ordinal() < states.get(i).ordinal()) {
                    letterState.put(c, states.get(i));
                }
            } else {
                letterState.put(c, states.get(i));
            }
        }
    }

    /**
     * Returns the amount of guesses that have been submitted
     *
     * @return as int
     */
    public int guessesMade() {
        return submittedGuesses.size();
    }

    /**
     * Returns the amount of guesses that still can be made
     *
     * @return as int
     */
    public int remainingGuesses() {
        return maxGuessAmount - guessesMade();
    }

    /**
     * Adds a letter to the current unsubmitted guess
     *
     * @param character the letter to be added as char
     */
    public void addCharacter(char character) {
        if (canAddCharacter()) {
            unsubmittedGuess += character;
            unsubmittedGuess = unsubmittedGuess.toUpperCase();
        }
    }

    /**
     * Removes the last char from the current unsubmitted guess
     */
    public void removeLastCharacter() {
        if (!unsubmittedGuess.isEmpty()) {
            unsubmittedGuess = unsubmittedGuess.substring(0, unsubmittedGuess.length() - 1);
        }
    }

    private boolean canAddCharacter() {
        return unsubmittedGuess.length() < answerLength;
    }

    public boolean canSubmitGuess() {
        return remainingGuesses() > 0;
    }

    public int getAnswerLength() {
        return answerLength;
    }

    public int getMaxGuessAmount() {
        return maxGuessAmount;
    }

    public Map<Character, CharacterPosition> getLetterState() {
        return letterState;
    }
}
