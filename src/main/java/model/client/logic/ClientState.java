package model.client.logic;

import java.util.*;
import model.client.Feature;
import model.general.message.client.ServerMessageInterpreter;
import model.client.notification.GameEventListener;
import model.general.message.server.*;

/**
 * Defines the behavior and state transitions for the client-side game logic.
 * Different states of the game logic implement this interface to handle various game events and actions.
 */
public abstract class ClientState implements ServerMessageInterpreter, GameEventListener {
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
     * Method to be overridden by subclasses for pre-exit initialization.
     * By default, it does nothing, but it can be overridden in derived states.
     */
    void exit(){
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

    /**
     * Reports the effect of a StartGameResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(StartGameResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received StartGameResponse not allowed in {0}", getName()); //NON-NLS
    }

    /**
     * Reports the effect of a LoginResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(LoginResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received LoginResponse not allowed in {0}", getName()); //NON-NLS
    }

    /**
     * Reports the effect of a DisconnectResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(DisconnectResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received DisconnectResponse not allowed in {0}", getName()); //NON-NLS
    }

    /**
     * Reports the effect of a GuessResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(GuessResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received GuessResponse not allowed in {0}", getName()); //NON-NLS
    }

    /**
     * Reports the effect of a ConnectionResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(ConnectionResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received ConnectionResponse not allowed in {0}", getName()); //NON-NLS
    }

    /**
     * Reports the effect of a StatsRequestResponse from the server message.
     *
     * @param msg the message received from server
     */
    public void received(StatsRequestResponse msg) {
        ClientGameLogic.LOGGER.log(System.Logger.Level.ERROR, "received StatsRequestResponse not allowed in {0}", getName()); //NON-NLS
    }
}
