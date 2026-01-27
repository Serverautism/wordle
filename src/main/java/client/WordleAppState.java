package client;

import client.view.ColoredTextTile;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import model.client.CurrentSession;
import model.client.notification.*;
import model.general.config.CharacterPosition;

import java.util.List;

public class WordleAppState extends GameAppState implements GameEventListener {
    /**
     * Logger for notifications and errors.
     */
    private static final System.Logger LOGGER = System.getLogger(WordleAppState.class.getName());

    private static final int GUESS_TILE_SIZE = 50;
    private static final int GUESS_TILE_GAP = 10;
    private static final int KEY_TILE_SIZE = 25;
    private static final int KEY_TILE_GAP = 5;

    private static final ColorRGBA GREEN = new ColorRGBA(108 / 255f, 169 / 255f, 101 / 255f, 1f);
    private static final ColorRGBA YELLOW = new ColorRGBA(200 / 255f, 182 / 255f, 83 / 255f, 1f);
    private static final ColorRGBA GREY = new ColorRGBA(120 / 255f, 124 / 255f, 127 / 255f, 1f);
    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(221 / 255f, 200 / 255f, 196 / 255f, 1f);

    private ColoredTextTile[][] guessGrid;
    private ColoredTextTile[][] letterGrid;

    /**
     * The root node for all visual elements in this state.
     */
    private final Node viewNode = new Node("view");

    /**
     * The node for the guess grid
     */
    private final Node guessNode = new Node("Guesses");

    /**
     * The node for the guess grid
     */
    private final Node letterNode = new Node("Letters");

    private CurrentSession gameSession;

    /**
     * This method is called when the state is enabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is enabled.
     */
    @Override
    protected void enableState() {
        viewNode.detachAllChildren();
        getGameLogic().getEventBroker().addListener(this);
        viewNode.attachChild(guessNode);
        viewNode.attachChild(letterNode);
        getApp().getGuiNode().attachChild(viewNode);
        addBackground();
    }

    /**
     * This method is called when the state is disabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is disabled.
     */
    @Override
    protected void disableState() {
        getApp().getGuiNode().detachChild(viewNode);
        getGameLogic().getEventBroker().removeListener(this);
    }

    /**
     * Does nothing for now.
     *
     * @param tpf the time per frame (seconds)
     */
    @Override
    public void update(float tpf) {}

    @Override
    public void receivedEvent(StartGameEvent event) {
        LOGGER.log(System.Logger.Level.INFO, "StartGameEvent received by view");
        gameSession = event.session();
        guessNode.detachAllChildren();
        letterNode.detachAllChildren();
        initializeGuessGrid(gameSession.getMaxGuessAmount(), gameSession.getAnswerLength());
        initializeLetterGrid();
    }

    @Override
    public void receivedEvent(GuessSubmittedEvent event) {
        LOGGER.log(System.Logger.Level.INFO, "GuessSubmittedEvent received by view");
        List<List<CharacterPosition>> positions = event.session().getPositions();
        List<String> guesses = event.session().getSubmittedGuesses();
        for (int row = 0; row < guesses.size(); row++) {
            for (int col = 0; col < event.session().getAnswerLength(); col++) {
                ColoredTextTile tile = guessGrid[row][col];
                CharacterPosition positionRating = positions.get(row).get(col);
                String text = "" + guesses.get(row).charAt(col);
                ColorRGBA color = switch (positionRating) {
                    case RIGHT -> GREEN;
                    case WRONG -> YELLOW;
                    case FUCKINGWRONG -> GREY;
                };
                tile.setColor(color);
                tile.setText(text);
                tile.setTextColor(ColorRGBA.White);
            }
        }
    }

    @Override
    public void receivedEvent(InputUpdateEvent event) {
        LOGGER.log(System.Logger.Level.INFO, "GuessSubmittedEvent received by view");
        List<String> guesses = event.session().getSubmittedGuesses();
        String currentText = event.session().getUnsubmittedGuess();
        int row = guesses.size();
        for (int col = 0; col < event.session().getAnswerLength(); col++) {
            ColoredTextTile tile = guessGrid[row][col];
            if (col < currentText.length()) {
                tile.setText("" + currentText.charAt(col));
            } else {
                tile.setText("");
            }
        }
    }

    private void initializeGuessGrid(int rows, int cols) {
        guessGrid = new ColoredTextTile[rows][cols];
        int startX = getApp().getConfig().getResolutionWidth() / 2 - (cols * (GUESS_TILE_SIZE + GUESS_TILE_GAP) / 2);
        int startY = getApp().getConfig().getResolutionHeight() - GUESS_TILE_GAP - GUESS_TILE_SIZE;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (GUESS_TILE_SIZE + GUESS_TILE_GAP);
                int y = startY - row * (GUESS_TILE_SIZE + GUESS_TILE_GAP);
                Geometry g = createQuad(GUESS_TILE_SIZE, ColorRGBA.White);
                BitmapText t = createText(32, "", ColorRGBA.Black);
                guessGrid[row][col] = new ColoredTextTile(GUESS_TILE_SIZE, g, t, guessNode, x, y);
            }
        }
    }

    private void initializeLetterGrid() {
        int rows = 2;
        int cols = 13;
        int letter = 'A';
        letterGrid = new ColoredTextTile[rows][cols];
        int startX = getApp().getConfig().getResolutionWidth() / 2 - (cols * (KEY_TILE_SIZE + KEY_TILE_GAP) / 2);
        int startY = 2 * (GUESS_TILE_GAP + KEY_TILE_SIZE);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = startX + col * (KEY_TILE_SIZE + KEY_TILE_GAP);
                int y = startY - row * (KEY_TILE_SIZE + KEY_TILE_GAP);
                String text = "" + (char) letter;
                letter += 1;
                Geometry g = createQuad(KEY_TILE_SIZE, ColorRGBA.White);
                BitmapText t = createText(16, text, ColorRGBA.Black);
                letterGrid[row][col] = new ColoredTextTile(KEY_TILE_SIZE, g, t, letterNode, x, y);
            }
        }
    }

    private void addBackground() {
        int width = getApp().getConfig().getResolutionWidth();
        int height = getApp().getConfig().getResolutionHeight();
        Geometry geom = createQuad(width, height, BACKGROUND_COLOR);
        geom.setLocalTranslation(0, 0, -1);
        viewNode.attachChild(geom);
    }

    /**
     * Creates a quad with specified size and color
     *
     * @param size in pixel
     * @param color as rgba
     * @return the quad geometry
     */
    private Geometry createQuad(int size, ColorRGBA color) {
        return createQuad(size, size, color);
    }

    /**
     * Creates a quad with specified size and color
     *
     * @param width the width as int
     * @param height the height as int
     * @param color the color as colorRGBA
     * @return the quad as geometry
     */
    private Geometry createQuad(int width, int height, ColorRGBA color) {
        Quad shape = new Quad(width, height);
        Geometry geom = new Geometry("Tile", shape);
        geom.setMaterial(createColoredMaterial(color));
        return geom;
    }

    /**
     *  Creates a BitmapText object with:
     *
     * @param fontSize fontsize as int
     * @param text as String
     * @param color as rgba
     * @return the BitmapText object
     */
    private BitmapText createText(int fontSize, String text, ColorRGBA color) {
        BitmapFont font = getApp().getAssetManager().loadFont("Metropolis-Bold-32.fnt");
        BitmapText t = new BitmapText(font);
        t.setText(text);
        t.setColor(color);
        t.setSize(fontSize);
        return t;
    }

    /**
     * Creates colored material
     *
     * @param color materials color as rgba
     * @return the material
     */
    private Material createColoredMaterial(ColorRGBA color) {
        final Material material = new Material(getApp().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if (color.getAlpha() < 1f) {
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        material.setColor("Color", color);
        return material;
    }
}
