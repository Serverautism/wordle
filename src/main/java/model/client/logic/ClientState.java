package model.client.logic;

import java.util.*;
import model.client.Feature;
import model.client.message.ServerMessageInterpreter;

/**
 * Defines the behavior and state transitions for the client-side game logic.
 * Different states of the game logic implement this interface to handle various game events and actions.
 */
public abstract class ClientState implements ServerMessageInterpreter {
    /**
     * The game logic object.
     */
    ClientGameLogic logic;

    /**
     * Constructs a client state of the specified game logic.
     *
     * @param logic the game logic
     */
    ClientState(ClientGameLogic logic) {
        this.logic = logic;
    }

    /**
     * Method to be overridden by subclasses for post-transition initialization.
     * By default, it does nothing, but it can be overridden in derived states.
     */
    void entry(){
        // Default implementation does nothing
    }

    /**
     * Update method for this state
     *
     * @param delta time per frame
     */
    public void update(float delta) {}

    /**
     * Returns the name of the current state.
     *
     * @return the name of the current state
     */
    String getName() {
        return getClass().getSimpleName();
    }

    /**
     * Returns the set of all features of this state.
     *
     * @return a set of features.
     */
    Set<Feature> getFeatures() {
        return Collections.emptySet();
    }

//    /**
//     * Reports the effect of a disconnect based on the server message.
//     *
//     * @param msg the message containing the effect of the disconnect
//     */
//    public void received(DisconnectEffectMsg msg) {
//        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "receivedDisconnectEffectMsg not allowed in {0}", getName()); //NON-NLS
//    }
}
