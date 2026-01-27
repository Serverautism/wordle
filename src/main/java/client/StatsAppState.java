package client;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import model.client.notification.GameEventListener;

public class StatsAppState extends GameAppState implements GameEventListener {
    /**
     * Logger for notifications and errors.
     */
    private static final System.Logger LOGGER = System.getLogger(WordleAppState.class.getName());

    private static final ColorRGBA GREEN = new ColorRGBA(108 / 255f, 169 / 255f, 101 / 255f, 1f);
    private static final ColorRGBA YELLOW = new ColorRGBA(200 / 255f, 182 / 255f, 83 / 255f, 1f);
    private static final ColorRGBA GREY = new ColorRGBA(120 / 255f, 124 / 255f, 127 / 255f, 1f);
    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(221 / 255f, 200 / 255f, 196 / 255f, 1f);

    /**
     * The root node for all visual elements in this state.
     */
    private final Node viewNode = new Node("view");

    /**
     * The node for the guess grid
     */
    private final Node statsNode = new Node("Stats");

    /**
     * This method is called when the state is enabled.
     * It is meant to be overridden by subclasses to perform
     * specific actions when the state is enabled.
     */
    @Override
    protected void enableState() {
        viewNode.detachAllChildren();
        getGameLogic().getEventBroker().addListener(this);
        viewNode.attachChild(statsNode);
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

    private void addBackground() {
        int width = getApp().getConfig().getResolutionWidth();
        int height = getApp().getConfig().getResolutionHeight();
        Geometry geom = createQuad(width, height, BACKGROUND_COLOR);
        geom.setLocalTranslation(0, 0, -1);
        viewNode.attachChild(geom);
    }
}
