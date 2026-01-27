package client;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import model.client.notification.GameEventListener;
import model.client.notification.StatsReceivedEvent;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatsAppState extends GameAppState implements GameEventListener {
    /**
     * Logger for notifications and errors.
     */
    private static final System.Logger LOGGER = System.getLogger(WordleAppState.class.getName());

    private static final ColorRGBA GREEN = new ColorRGBA(108 / 255f, 169 / 255f, 101 / 255f, 1f);
    private static final ColorRGBA YELLOW = new ColorRGBA(200 / 255f, 182 / 255f, 83 / 255f, 1f);
    private static final ColorRGBA GREY = new ColorRGBA(120 / 255f, 124 / 255f, 127 / 255f, 1f);
    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(221 / 255f, 200 / 255f, 196 / 255f, 1f);

    private static final int BAR_HEIGHT = 30;
    private static final int BAR_GAP = 10;

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

    @Override
    public void receivedEvent(StatsReceivedEvent event) {
        final int score = event.msg().getScore();
        final int streak = event.msg().getStreak();
        final int maxStreak = event.msg().getMaxStreak();
        final int wordlesSolved = event.msg().getWordlesSolved();
        final int wordlesLost = event.msg().getWordlesLost();
        final int[] guessDistribution = event.msg().getGuessDistribution();

        createTextElements(score, streak, maxStreak, wordlesSolved, wordlesLost);
        createGuessDistribution(guessDistribution);
    }

    private void createTextElements(int score, int streak, int maxStreak, int wordlesSolved, int wordlesLost) {
        Node scoreText = createTwoElementText("" + score, "score");
        Node winrateText = createTwoElementText(Math.round(((float) wordlesSolved / (wordlesSolved + wordlesLost)) * 100f) + "%", "winrate");
        Node matchesText = createTwoElementText("" + (wordlesSolved + wordlesLost), "played");
        Node streakText = createTwoElementText("" + streak, "current streak");
        Node maxStreakText = createTwoElementText("" + maxStreak, "max streak");

        List<Node> textList = new ArrayList<>();
        textList.add(scoreText);
        textList.add(winrateText);
        textList.add(matchesText);
        textList.add(streakText);
        textList.add(maxStreakText);

        float gapX = getApp().getConfig().getResolutionWidth() / 4f;
        float gapY = 50;
        float yStart = getApp().getConfig().getResolutionHeight();

        int index = 0;
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 3; j ++) {
                if (index < textList.size()) {
                    Node n = textList.get(index);
                    n.setLocalTranslation(i * gapX, yStart - (j * gapY * 2f), 0);
                    viewNode.attachChild(n);
                }
                index += 1;
            }
        }
    }

    private void createGuessDistribution(int[] guessDistribution) {
        Node distributionNode = new Node("distribution");

        int sum = Arrays.stream(guessDistribution).sum();
        float y = getApp().getConfig().getResolutionHeight() - ((float) getApp().getConfig().getResolutionHeight() / 2);
        int index = 1;
        for (int i : guessDistribution) {
            Node row = createDistributionRow(index, i, sum);
            row.setLocalTranslation(50, y - index * (BAR_HEIGHT + BAR_GAP), 0);
            distributionNode.attachChild(row);
            index += 1;
        }

        viewNode.attachChild(distributionNode);
        distributionNode.setLocalTranslation(0, 0, 1);
    }

    private Node createDistributionRow(int guess, int amount, int sum) {
        float sideGap = getApp().getConfig().getResolutionWidth() * .05f;
        Node result = new Node("Row");

        BitmapText text = createText(32, "" + guess, ColorRGBA.Black);
        text.setLocalTranslation(-text.getLineWidth(), text.getLineHeight() / 2f, 0);
        result.attachChild(text);

        float totalRowSpace = getApp().getConfig().getResolutionWidth() - sideGap * 2 - 50;
        int barLength = Math.round(totalRowSpace * ((float) amount / sum));
        Geometry bar = createQuad(barLength, 20, GREEN);
        bar.setLocalTranslation(10, -BAR_HEIGHT / 3f, 0);
        result.attachChild(bar);

        return result;
    }

    private Node createTwoElementText(String textOne, String textTwo) {
        BitmapText tOne = createText(32, textOne, ColorRGBA.Black);
        BitmapText tTwo = createText(16, textTwo, ColorRGBA.Black);
        Node result = new Node(textOne + textTwo);
        result.attachChild(tOne);
        result.attachChild(tTwo);
        float textX = - tOne.getLineWidth() / 2f;
        float textY = tOne.getLineHeight();
        tOne.setLocalTranslation(textX, textY, 1);
        textX = - tTwo.getLineWidth() / 2f;
        textY = - tTwo.getLineHeight();
        tTwo.setLocalTranslation(textX, textY, 1);
        return result;
    }

    private void addBackground() {
        int width = getApp().getConfig().getResolutionWidth();
        int height = getApp().getConfig().getResolutionHeight();
        Geometry geom = createQuad(width, height, BACKGROUND_COLOR);
        geom.setLocalTranslation(0, 0, -1);
        viewNode.attachChild(geom);
    }
}
