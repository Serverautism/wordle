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
    private List<String> words = new ArrayList<>();

    /**
     * Set containing all possible words
     */
    private final Set<String> wordsSet = new HashSet<>();

    /**
     * List containing all possible answers
     */
    private List<String> answers;

    /**
     * Set containing all possible answers
     */
    private final Set<String> answersSet = new HashSet<>();

    /**
     * List containing all possible guesses
     */
    private List<String> guesses;

    /**
     * Set containing all possible guesses
     */
    private final Set<String> guessesSet = new HashSet<>();

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
        final int index = new Random(lastDate.toEpochDay()).nextInt(answers.size());
        currentWord = answers.get(index);
        LOGGER.log(System.Logger.Level.INFO, "Today´s word is: {0}", currentWord);
    }

    /**
     * Returns the wordle of the day
     */
    public String getCurrentWord() {
        return currentWord;
    }

    /**
     * Returns a random answer from the list
     */
    public String getRandomWord() {
        final int index = new Random().nextInt(answers.size());
        return answers.get(index);
    }

    /**
     * Evaluates a guess based on a given answer
     *
     * @param guess guessed word
     * @param answer answer word
     * @return List of CharacterPosition for each char
     */
    public List<CharacterPosition> evaluateGuess(String guess, String answer) {
        if (!(guess.length() == answer.length())) {
            throw new IllegalArgumentException("guess length must be the same as answer length");
        }

        CharacterPosition[] result = new CharacterPosition[answer.length()];
        Map<Character, Integer> unmatched = new HashMap<>();
        for (int i = 0; i < answer.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {
                result[i] = CharacterPosition.RIGHT;
            } else {
                unmatched.put(answer.charAt(i), unmatched.getOrDefault(answer.charAt(i), 0) + 1);
            }
        }

        for (int i = 0; i < answer.length(); i++) {
            if (result[i] == CharacterPosition.RIGHT) continue;
            if (unmatched.containsKey(guess.charAt(i))  && unmatched.get(guess.charAt(i)) > 0) {
                result[i] = CharacterPosition.WRONG;
                unmatched.put(guess.charAt(i), unmatched.get(guess.charAt(i)) - 1);
            } else {
                result[i] = CharacterPosition.FUCKINGWRONG;
            }
        }

        return Arrays.stream(result).toList();
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
     * Returns the current date as epoch day
     *
     * @return epoch day as long
     */
    public long getCurrentPlayDay() {
        return lastDate.toEpochDay();
    }

    /**
     * Loads all possible word from the configured path
     */
    private void loadWords() {
        final String answerPath = config.getAnswerListPath();
        final String guessPath = config.getGuessListPath();

        try {
            LOGGER.log(System.Logger.Level.INFO, "Trying to load wordle answer list from: {0}", answerPath);
            answers = Files.readAllLines(Paths.get(answerPath), StandardCharsets.UTF_8)
                    .stream()
                    .map(String::strip)
                    .map(String::toUpperCase)
                    .toList();
            answersSet.addAll(answers);
            LOGGER.log(System.Logger.Level.INFO, "Successfully loaded {0} answers", answers.size());

            LOGGER.log(System.Logger.Level.INFO, "Trying to load wordle guess list from: {0}", guessPath);
            guesses = Files.readAllLines(Paths.get(guessPath), StandardCharsets.UTF_8)
                    .stream()
                    .map(String::strip)
                    .map(String::toUpperCase)
                    .toList();
            guessesSet.addAll(guesses);
            LOGGER.log(System.Logger.Level.INFO, "Successfully loaded {0} guesses", guesses.size());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load wordlist", e);
        }

        words.addAll(answers);
        words.addAll(guesses);
        wordsSet.addAll(words);

        LOGGER.log(System.Logger.Level.INFO, "Successfully loaded {0}/{1} words", wordsSet.size(), words.size());
    }
}
