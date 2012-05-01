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

package ramshteks.java.patterns;

import ramshteks.java.patterns.interfaces.IName;

import java.util.HashMap;

/**
 * Standard pattern of strategy
 */
public final class Strategy {

	private HashMap<String, Method> handlers;

	public Strategy() {
		handlers = new HashMap<String, Method>();
	}

	/**
	 * Executing method
	 *
	 * @param what object
	 */
	public void execute(IName what) {
		if (handlers.containsKey(what.name())) {
			handlers.get(what.name()).executeMethod(what);
		}
	}

	/*public void execute(String name, Object arg){
		if(handlers.containsKey(name)){
			handlers.get(name).executeMethod();
		}
	}*/

	/**
	 * Adding new method handler
	 *
	 * @param name	 name of strategy
	 * @param delegate delegate
	 */
	public void addHandler(String name, Method delegate) {

		if (delegate == null) throw new IllegalArgumentException("Delegate is null");
		if (!handlers.containsKey(name)) {
			handlers.put(name, delegate);
		}
	}

	/**
	 * Removing strategy handler by name
	 *
	 * @param name name of strategy
	 */
	public void removeHandler(String name) {
		if (handlers.containsKey(name)) {
			handlers.remove(name);
		}
	}

	/**
	 * Standard interface for method realization
	 */
	public static interface Method {
		/**
		 * Be man, implement this
		 *
		 * @param arg object
		 */
		void executeMethod(IName arg);
	}

	/**
	 * Standard implementation of IName
	 */
	public static class Name implements IName {

		private String name;

		public Name(String name) {
			this.name = name;
		}

		public String name() {
			return name;
		}
	}
}
