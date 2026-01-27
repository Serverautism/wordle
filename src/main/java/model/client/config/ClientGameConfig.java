package model.client.config;

import model.general.config.Config;

public class ClientGameConfig extends Config {
    /**
     * The username to be used.
     */
    @Property("name")
    private String name = "";

    /**
     * The password to be used.
     */
    @Property("password")
    private String password = "";

    /**
     * The default port number for the game server.
     */
    @Property("hostname")
    private String hostname = "127.0.0.1";

    /**
     * The default port number for the game server.
     */
    @Property("port")
    private int port = 1234;

    /**
     * The width of the game view resolution in pixels.
     */
    @Property("settings.resolution.width")
    private int resolutionWidth = 1200;

    /**
     * The height of the game view resolution in pixels.
     */
    @Property("settings.resolution.height")
    private int resolutionHeight = 800;

    /**
     * Specifies whether the game should start in full-screen mode.
     */
    @Property("settings.full-screen")
    private boolean fullScreen = false;

    /**
     * Specifies whether gamma correction should be enabled.
     * If enabled, the main framebuffer is configured for sRGB colors,
     * and sRGB images are linearized.
     * Requires a GPU that supports GL_ARB_framebuffer_sRGB; otherwise, this setting will be ignored.
     */
    @Property("settings.use-gamma-correction")
    private boolean useGammaCorrection = true;

    /**
     * Specifies whether full resolution framebuffers should be used on Retina displays.
     * This setting is ignored on non-Retina platforms.
     */
    @Property("settings.use-retina-framebuffer")
    private boolean useRetinaFrameBuffer = false;

    /**
     * Specifies whether the settings window should be shown for configuring the game.
     */
    @Property("settings.show")
    private boolean showSettings = false;

    /**
     * Specifies whether the JME statistics window should be shown in the lower left corner of the screen.
     */
    @Property("statistics.show")
    private boolean showStatistics = false;

    public boolean getShowSettings() {
        return showSettings;
    }

    public boolean isShowStatistics() {
        return showStatistics;
    }

    public boolean isUseRetinaFrameBuffer() {
        return useRetinaFrameBuffer;
    }

    public boolean isUseGammaCorrection() {
        return useGammaCorrection;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public int getResolutionHeight() {
        return resolutionHeight;
    }

    public int getResolutionWidth() {
        return resolutionWidth;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
