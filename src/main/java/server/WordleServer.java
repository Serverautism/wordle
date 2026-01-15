package server;

import client.WordleApp;
import com.jme3.app.SimpleApplication;

public class WordleServer extends SimpleApplication{
    public static void main(String[] args) {
        WordleApp app = new WordleApp();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        setPauseOnLostFocus(false);
    }
}
