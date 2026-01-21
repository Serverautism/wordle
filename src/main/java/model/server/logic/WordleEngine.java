package model.server.logic;

import model.general.config.CharacterPosition;
import model.server.config.ServerGameConfig;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class WordleEngine {
    /**
     * Logger for events and errors
     */
    public static System.Logger LOGGER = System.getLogger(WordleEngine.class.getName());

    /**
     * Today´s date in UTC time zone
     */
    private LocalDate lastDate = LocalDate.now(ZoneOffset.UTC);

    /**
     * List containing all possible words
     */
    private List<String> words;

    /**
     * Set containing all possible words
     */
    private final Set<String> wordsSet = new HashSet<>();

    /**
     * The word of the day
     */
    private String currentWord;

    /**
     *
     */
    private ServerGameConfig config;

    /**
     * Creates new WordleEngine
     *
     * @param config the servers game config
     */
    public WordleEngine(ServerGameConfig config) {
        this.config = config;
        loadWords();
        determineDailyWord();
    }

    public void update(float tpf) {
        LocalDate currentDate = LocalDate.now(ZoneOffset.UTC);
        if (!currentDate.equals(lastDate)) {
            LOGGER.log(System.Logger.Level.INFO, "Date changed");
            lastDate = currentDate;
            determineDailyWord();
        }
    }

    /**
     * Determines daily word based on the date
     */
    private void determineDailyWord() {
        final int index = new Random(lastDate.toEpochDay()).nextInt(words.size());
        currentWord = words.get(index);
        LOGGER.log(System.Logger.Level.INFO, "Today´s word is: {0}", currentWord);
    }

    /**
     * Returns the wordle of the day
     */
    public String getCurrentWord() {
        return currentWord;
    }

    /**
     * Returns a random word from the list
     */
    public String getRandomWord() {
        final int index = new Random().nextInt(words.size());
        return words.get(index);
    }

    /**
     * Evaluates a guess based on a given answer
     *
     * @param guess guessed word
     * @param answer answer word
     * @return List of CharacterPosition for each char
     */
    public List<CharacterPosition> evaluateGuess(String guess, String answer) {
        List<CharacterPosition> result = new ArrayList<>();
        List<Character> evaluatedChars = new ArrayList<>();
        for (int i = 0; i < answer.length(); i++) {
            char guessChar = guess.toUpperCase().toCharArray()[i];
            char answerChar = answer.toUpperCase().toCharArray()[i];
            if (guessChar == answerChar) {
                result.add(CharacterPosition.RIGHT);
            } else if (answer.contains(guess)) {
                int amountGuess = (int) guess.chars().filter(c -> c == guessChar).count();
                int amountAnswer = (int) answer.chars().filter(c -> c == guessChar).count();
                if (amountGuess <= amountAnswer) {
                    result.add(CharacterPosition.WRONG);
                }
            } else {
                result.add(CharacterPosition.FUCKINGWRONG);
            }
            evaluatedChars.add(guessChar);
        }
        return result;
    }

    /**
     * Checks if a given word is a valid guess
     *
     * @param guess the word that was entered
     * @return true if the word is valid, false otherwise
     */
    public boolean isValidWord(String guess) {
        return wordsSet.contains(guess);
    }

    /**
     * Loads all possible word from the configured path
     */
    private void loadWords() {
        final String path = config.getWordListPath();
        LOGGER.log(System.Logger.Level.INFO, "Trying to load new wordle from: {0}", path);

        try {
            words = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)
                    .stream()
                    .map(String::strip)
                    .map(String::toUpperCase)
                    .toList();
            wordsSet.addAll(words);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load wordlist", e);
        }

        LOGGER.log(System.Logger.Level.INFO, "Successfully loaded {0} words", words.size());
    }
}
