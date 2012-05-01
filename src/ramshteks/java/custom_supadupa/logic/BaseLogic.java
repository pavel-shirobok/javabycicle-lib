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

package ramshteks.java.custom_supadupa.logic;

import ramshteks.java.supadupa.core.Core;
import ramshteks.java.supadupa.core.SDError;

/**
 * Just logging event logic implementation
 */
public class BaseLogic implements Core.ILogic {

	private Core.ILogger logger;
	private Core.IAgent agent;

	public final void setAgent(Core.IAgent agent) throws IllegalAccessError {
		if (this.agent != null) throw new IllegalAccessError("Agent is already installed");
		this.agent = agent;
		logger = agent().logger();
	}

	public void onConnectionConnect(int cid) {
		logger().log("Connected: " + cid);
	}

	public void onRawData(int cid, byte[] bytes) {

	}

	public void onConnectionDisconnect(int cid) {
		logger().log("Disconnected: " + cid);
	}

	public void onConnectionError(int cid, SDError error) {
		logger().log("Error: " + cid + " " + error);
	}

	public void onConnectionPacketReceived(int cid, Core.IPacket packet) {
		logger().log("Receive: cid=" + cid);
	}

	public void onCoreStartTick() {
	}

	public void onCoreEndTick() {
	}

	public void onCoreStart() {
		logger().log("Core started");
	}

	protected final Core.ILogger logger() {
		return logger;
	}

	protected final Core.IAgent agent() {
		return agent;
	}
}
