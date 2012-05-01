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

package ramshteks.java.custom_supadupa.logger;

import ramshteks.java.custom_supadupa.logger.writers.Writers;
import ramshteks.java.supadupa.core.Core;

public class SupaDupaLogger implements Core.ILogger {

	private Writers.IWriter writer;
	private String loggerName;

	public SupaDupaLogger(String loggerName, Writers.IWriter writer) {
		this.writer = writer;
		this.loggerName = loggerName;
	}

	public void log(Exception except) {
		write(Core.LoggerLevel.WARNING.label() +" "+ formatException(except));
	}

	public void log(String message, Exception except) {
		write(Core.LoggerLevel.WARNING.label() +" "+ formatMessage(message) +" "+ formatException(except));
	}

	public void log(String message, Exception except, Core.LoggerLevel level) {
		write(level.label() +" "+ formatMessage(message) +" "+ formatException(except));
	}

	public void log(String message) {
		write(Core.LoggerLevel.INFO.label() +" "+ formatMessage(message));
	}

	public void log(String message, Core.LoggerLevel level) {
		write(level.label() +" "+ formatMessage(message));
	}

	private void write(String mess) {
		writer.write("[" + loggerName + "]>>" + mess);
	}

	private String formatMessage(String mess) {

		return mess;

	}

	private String formatException(Exception exc) {
		String result = "";
		StackTraceElement[] element = exc.getStackTrace();
		for(int i=0; i< element.length; i++ ){

			result += ">>" + element[i].toString()+"\n";
		}


		return exc.toString() + ": \n" + result;
	}
}
