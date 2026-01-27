package client;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import model.client.logic.ClientGameLogic;

/**
 * Abstract class representing a state in the game.
 * Extends the AbstractAppState from jMonkeyEngine to manage state behavior.
 */
public abstract class GameAppState extends AbstractAppState {
    /**
     * Reference to the main app.
     */
    private WordleApp app;

    /**
     * Creates a new GameAppState that is initially disabled.
     *
     * @see #setEnabled(boolean)
     */
    protected GameAppState() {
        setEnabled(false);
    }

    /**
     * Initializes the state manager and application.
     *
     * @param stateManager The state manager
     * @param application  The application instance
     */
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        this.app = (WordleApp) application;
        if (isEnabled()) enableState();
    }

    /**
     * Returns the GameApp instance associated with this GameAppState.
     *
     * @return The GameApp instance.
     */
    public WordleApp getApp() {
        return app;
    }

    /**
     * Returns the client game logic handler.
     *
     * @return the client game logic handler
     */
    public ClientGameLogic getGameLogic() {
        return app.getGameLogic();
    }

    /**
     * Sets the enabled state of the GameAppState.
     * If the new state is the same as the current state, the method returns.
     *
     * @param enabled The new enabled state.
     */
    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() == enabled) return;
        super.setEnabled(enabled);
        if (app != null) {
            if (enabled)
                enableState();
            else
                disableState();
        }
    }

    /**
     * This method is called when the state is enabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is enabled.
     */
    protected abstract void enableState();

    /**
     * This method is called when the state is disabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is disabled.
     */
    protected abstract void disableState();

    /**
     * Creates a quad with specified size and color
     *
     * @param size in pixel
     * @param color as rgba
     * @return the quad geometry
     */
    public Geometry createQuad(int size, ColorRGBA color) {
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
    public Geometry createQuad(int width, int height, ColorRGBA color) {
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
    public BitmapText createText(int fontSize, String text, ColorRGBA color) {
        BitmapFont font = app.getAssetManager().loadFont("Metropolis-Bold-32.fnt");
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
    public Material createColoredMaterial(ColorRGBA color) {
        final Material material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if (color.getAlpha() < 1f) {
            material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        }
        material.setColor("Color", color);
        return material;
    }
}
