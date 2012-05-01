/*
 * Copyright (C) 2011 Shirobok Pavel
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation,
 * Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * Contact information:
 * email: ramshteks@gmail.com
 */

package ramshteks.java.cozy;

import ramshteks.java.cozy.core.CozyCore;
import ramshteks.java.custom_supadupa.logic.BaseLogic;
import ramshteks.java.jravel.Ravel;
import ramshteks.java.patterns.Strategy;
import ramshteks.java.supadupa.core.Core;
import ramshteks.java.supadupa.core.SDError;

import java.util.Enumeration;
import java.util.Hashtable;

public class Cozy<CH extends CozyCore.ConnectionHandler> extends BaseLogic implements Core.ILogger{

    /**
     * Map of connections
     */
    private Hashtable<Integer, CH> connections;

    /**
     * List of used states
     */
    private Hashtable<String, CozyCore.State> states;

    /**
     * Reference to default state object
     */
    private CozyCore.State defaultState;

    /**
     * Factory for connection handlers
     */
    private CozyCore.ICHFactory<CH> handlerFactory;

	/**
	 * Ravel's init flag
	 */
	private boolean ravelAvailable = false;

	/**
	 * Ravel instance
	 */
	private Ravel ravel;

	/**
	 * Ravels listeners handler
	 */
	private Strategy ravelStrategy;

    /**
     * Constructor
     */
    public Cozy(Ravel ravel) {
        super();
        connections = new Hashtable<Integer, CH>();
        states = new Hashtable<String, CozyCore.State>();



		this.ravel = ravel;
		ravelAvailable = ravel!=null;
		if(ravelAvailable)ravelStrategy = new Strategy();
    }

    @Override
    public final void onCoreStart() {
        if (defaultState == null) {
            throw new NullPointerException("Default state not installed");
        }

        if (handlerFactory == null) {
            throw new NullPointerException("CH Factory not installed");
        }

        super.onCoreStart();
        onLogicStart();
    }


    @Override
    public final void onConnectionConnect(int cid) {
        super.onConnectionConnect(cid);
        if (!connections.containsKey(cid)) {
            CH handler = handlerFactory.getHandler(cid);
            handler.setCurrentState(defaultState);
            connections.put(cid, handler);
            onClientAdded(handler);
        }
    }

    @Override
    public final void onConnectionDisconnect(int cid) {
        super.onConnectionDisconnect(cid);
        if (connections.containsKey(cid)) {
            onClientRemoved(connections.remove(cid));
        }
    }

    @Override
    public final void onConnectionError(int cid, SDError error) {
        super.onConnectionError(cid, error);
    }

    @Override
    public final void onRawData(int cid, byte[] bytes) {
        super.onRawData(cid, bytes);
    }

    @Override
    public final void onConnectionPacketReceived(int cid, Core.IPacket packet) {
        CH handler = getHandlerById(cid);

        if (null != handler) {
            CozyCore.State state = handler.getCurrentState();
            state.execute(packet, cid);
        }
    }

    @Override
    public final void onCoreStartTick() {

		if(ravelAvailable){
			while (ravel.hasResult()){
				ravelStrategy.execute(new CozyCore.CozyTaskResult(ravel.nextResult()));
			}
		}

        onLogicStartTick();
    }

    @Override
    public final void onCoreEndTick() {
        onLogicEndTick();
    }

    //-----------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------
    // Custom cozy event handlers -------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------------------------------------------

	/**
	 * Logic started handler
	 */
    protected void onLogicStart() {}

	/**
	 * Logic 'start-tick' handler
	 */
    protected void onLogicStartTick(){}

	/**
	 * Logic 'end-tick' handler
	 */
    protected void onLogicEndTick(){}

	/**
	 * New connection handler
	 * @param handler instance of {@link CH} for new connection
	 */
    protected void onClientAdded(CH handler) {}

	/**
	 * Connection removed handler
	 * @param handler instance of {@link CH} for removed connection, for last compute
	 */
    protected void onClientRemoved(CH handler) {}

	/**
	 * Incoming {@link ramshteks.java.cozy.core.CozyCore.Request} handler
	 * @param request instance of incoming {@link ramshteks.java.cozy.core.CozyCore.Request}
	 */
    protected final void onRequestReceived(CozyCore.Request request) {}

	/**
	 * Add new task in queue
	 * @param task instance of {@link ramshteks.java.cozy.core.CozyCore.CozyTask}
	 */
	protected final void pushTask(CozyCore.CozyTask task){
		if(ravelAvailable){
			ravel.push(task);
		}else{
			throw new IllegalAccessError("Ravel module not initialized");
		}
	}

	/**
	 * Add listener for completed task
	 * @param task_name Task {@link String}'ed name
	 * @param listener implementation of {@link Strategy.Method},as handler for completed task.
	 * 		  Received instance of {@link ramshteks.java.cozy.core.CozyCore.CozyTaskResult}
	 */
	protected final void addTaskListener(String task_name, Strategy.Method listener){
		if(ravelAvailable){
			ravelStrategy.addHandler(task_name, listener);
		}else{
			throw new IllegalAccessError("Ravel module not initialized");
		}
	}

    /**
     * Sending packet to <param>cid</param>
     *
     * @param cid     target cid of connection
     * @param message message, guys!
     */
    protected final void send(int cid, Core.IPacket message) {
        if (containsCid(cid)) {
            agent().send(cid, message);
        }
    }

	/**
	 * Multicast sending to all handlers
	 * @param packet packet to multicast sending
	 */
	protected final void multicast(Core.IPacket packet){
		multicast(packet, null);
	}

	/**
	 * Multicast sending to all handlers
	 * @param packet packet to multicast sending
	 * @param rule Rule for selecting from all connections, instance of {@link IRule<CH>}
	 */
	protected final void multicast(Core.IPacket packet, IRule<CH> rule){
		Enumeration<CH> handlers = handlers();
		CH handler;
		if(rule==null){
			while (handlers.hasMoreElements()){
				send(handlers.nextElement().cid(), packet);
			}
		}else{
			while (handlers.hasMoreElements()){
				handler=handlers.nextElement();
				if(rule.checkRule(handler)){
					send(handler.cid(), packet);
				}
			}
		}
	}

    /**
     * Check for containing handler with specified cid
     *
     * @param cid id of connection
     * @return true if contains
     */
    protected final boolean containsCid(int cid) {
        return connections.containsKey(cid);
    }

    /**
     * Putting new state in collection
     *
     * @param state new instance of state
     */
    protected final void registerState(CozyCore.State state) {
        if (!states.containsKey(state.name())) {
            states.put(state.name(), state);
        }
    }

	/**
	 * Return current timestamp. Analog of System.currentTimeMillis();
	 * @return timestamp in millis
	 */
    protected final long timestamp() {
        return System.currentTimeMillis();
    }



    /**
     * Returned {@link ramshteks.java.cozy.core.CozyCore.State} object by name
     *
     * @param name name of searched state
     * @return instance if state exist or null if not
     */
    protected final CozyCore.State getStateByName(String name) {
        CozyCore.State result = null;
        if (states.containsKey(name)) {
            result = states.get(name);
        }
        return result;
    }

	/**
	 * Set state to specified connection
	 * @param cid id of connection
	 * @param state instance of {@link ramshteks.java.cozy.core.CozyCore.State}
	 */
	protected final void setState(int cid, CozyCore.State state){
		CH handler = getHandlerById(cid);
		if(handler!=null){
			handler.setCurrentState(state);
		}

	}

    /**
     * Returned default state object
     *
     * @return instance of default state
     */
    protected final CozyCore.State getDefaultState() {
        return defaultState;
    }

    /**
     * Set default state object
     *
     * @param defaultState instance of state
     */
    protected final void setDefaultState(CozyCore.State defaultState) {
        if (defaultState == null) {
            return;
        }
        this.defaultState = defaultState;
    }

    /**
     * Returns <code>Enumeration</code> keys for getting connection handler by cid
     *
     * @return enumeration of keys
     */
    protected final Enumeration<Integer> cids() {
        return connections.keys();
    }

    /**
     * Returns <code>Enumeration</code> elements for getting connection handler
     *
     * @return enumeration of handlers
     */
    protected final Enumeration<CH> handlers() {
        return connections.elements();
    }

    /**
     * Returned connection handler instance by csid
     *
     * @param cid cap!
     * @return connection handler or null
     */
    protected final CH getHandlerById(int cid) {
        CH handler = null;
        if (connections.containsKey(cid)) {
            handler = connections.get(cid);
        }
        return handler;
    }

    /**
     * Setting Connection handler factory
     *
     * @param cihFactory instance of <code>CozyCore.ConnectionHandlerFactory</code>
     */
    protected final void setCihFactory(CozyCore.ICHFactory<CH> cihFactory) {
        this.handlerFactory = cihFactory;
    }

	public void log(String message, Exception except) {
		logger().log(message, except);
	}

	public void log(String message, Exception except, Core.LoggerLevel level) {
		logger().log(message, except, level);
	}

	public void log(String message) {
		logger().log(message);
	}

	public void log(String message, Core.LoggerLevel level) {
		logger().log(message, level);
	}

	public static interface IRule<CH>{
		boolean checkRule(CH connectionHandler);
	}
}
