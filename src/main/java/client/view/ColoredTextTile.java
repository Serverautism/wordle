package client.view;

import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class ColoredTextTile {
    private final Geometry geometry;
    private final BitmapText text;
    private final Node guiNode;
    private final int size;
    private int x;
    private int y;

    public ColoredTextTile(int size, Geometry geom, BitmapText text, Node guiNode) {
        geometry = geom;
        this.text = text;
        this.guiNode = guiNode;
        this.size = size;

        guiNode.attachChild(geometry);
        guiNode.attachChild(this.text);
    }

    public ColoredTextTile(int size, Geometry geom, BitmapText text, Node guiNode, int x, int y) {
        this(size, geom, text, guiNode);
        this.x = x;
        this.y = y;
        setLocation(x, y);
    }

    /**
     * Changes the tiles color
     *
     * @param color specified color
     */
    public void setColor(ColorRGBA color) {
        geometry.getMaterial().setColor("Color", color);
    }

    /**
     * Changes the texts color
     *
     * @param color specified color
     */
    public void setTextColor(ColorRGBA color) {
        text.setColor(color);
    }

    /**
     * Changes the tiles text
     *
     * @param text specified text
     */
    public void setText(String text) {
        this.text.setText(text);
        setTextLocation(x, y);
    }

    /**
     * Places the tile at
     *
     * @param x int
     * @param y int
     */
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        geometry.setLocalTranslation(x, y, 0);
        setTextLocation(x, y);
    }

    private void setTextLocation(int x, int y) {
        final float textX = x + size / 2f - text.getLineWidth() / 2f;
        final float textY = y + size / 2f + text.getLineHeight() / 2f;
        text.setLocalTranslation(textX, textY, 1);
    }
}
