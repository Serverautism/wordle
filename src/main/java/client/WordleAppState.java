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
import model.client.notification.GameEventListener;
import model.client.notification.GuessSubmittedEvent;
import model.client.notification.InputUpdateEvent;
import model.client.notification.StartGameEvent;
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

    private static final ColorRGBA GREEN = new ColorRGBA(132 / 255f, 176 / 255f, 130 / 255f, 1f);
    private static final ColorRGBA YELLOW = new ColorRGBA(255 / 255f, 182 / 255f, 92 / 255f, 1f);
    private static final ColorRGBA GREY = new ColorRGBA(76 / 255f, 87 / 255f, 82 / 255f, 1f);
    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(221 / 255f, 200 / 255f, 196 / 255f, 1f);

    private ColoredTextTile[][] guessGrid;

    /**
     * The root node for all visual elements in this state.
     */
    private final Node viewNode = new Node("view");

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
        getApp().getGuiNode().attachChild(viewNode);
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
        viewNode.detachAllChildren();
        initializeGuessGrid(gameSession.getMaxGuessAmount(), gameSession.getAnswerLength());
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
                Geometry g = createQuad(GUESS_TILE_SIZE, GREY);
                BitmapText t = createText(32, "", ColorRGBA.Black);
                guessGrid[row][col] = new ColoredTextTile(GUESS_TILE_SIZE, g, t, viewNode, x, y);
            }
        }
    }

    /**
     * Creates a quad with specified size and color
     *
     * @param size in pixel
     * @param color as rgba
     * @return the quad geometry
     */
    private Geometry createQuad(int size, ColorRGBA color) {
        Quad shape = new Quad(size, size);
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
