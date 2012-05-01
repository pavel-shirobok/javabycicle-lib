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

package ramshteks.java.cozy.core;

import ramshteks.java.jravel.core.RavelCore;
import ramshteks.java.patterns.Strategy;
import ramshteks.java.patterns.interfaces.IName;
import ramshteks.java.supadupa.core.Core;
import ramshteks.java.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Hashtable;

public class CozyCore {

    public static class ConnectionHandler{

        private State currentState;
        private int cid;

        public ConnectionHandler(int cid){
            this.cid = cid;
        }

        public void setCurrentState(State state) {
            this.currentState = state;

        }

        public State getCurrentState() {

            return  currentState;
        }

        public int cid() {
            return cid;
        }
    }

    public static interface ICHFactory<CH extends ConnectionHandler> {
		CH getHandler(int cid);
	}

    public static class State{
		private String stateName;
		private Strategy listeners;
		private Hashtable<Short, IParser> parsers;

		public State(String name){
			stateName = name;
			listeners = new Strategy();
			parsers = new Hashtable<Short, IParser>();
		}

		public void addParser(IParser parser, Strategy.Method listener){
			listeners.addHandler(parser.name(), listener);
			parsers.put(parser.commandCode(), parser);
		}

        public void execute(Core.IPacket packet, int cid) throws NullPointerException{

            IParser parser;
            Request request;

			byte[] bytes = packet.bytes();

            short code = ByteUtils.getShort(bytes);

            if(parsers.containsKey(code)){

				ByteBuffer byteBuffer = ByteBuffer.wrap(ByteUtils.getSubArray(bytes, 2));
				parser = parsers.get(code);
                request = parser.parse(cid, byteBuffer);

				if(request==null)throw new NullPointerException("Parser return null-packet");

                listeners.execute(request);
            }
        }

		public String name() {
			return stateName;
		}


    }

    public static interface IParser extends IName {
		Request parse(int cid, ByteBuffer bytes);
        short commandCode();
    }

    public static class Request implements IName {
        private String name;
        private int cid;
		private short command;

		public Request(short command, String name, int cid){
            this.name = name;
            this.cid = cid;
			this.command = command;
        }

        public final int cid(){
            return cid;
        }

        public final String name() {
            return name;
        }

		public final short command(){
			return command;
		}

    }

    public static class Response implements Core.IPacket{
        private short command;
		private byte[] bytes;

        public Response(int command){
            this.command = (short)command;
        }

		protected final void setBytes(byte[] bytes){
			this.bytes = bytes;
		}

        public final short command() {
            return command;
        }

        public final byte[] bytes() {
            return bytes;
        }
    }

	public static abstract class CozyTask implements RavelCore.RavelTask, IName {

		private int cid;
		private int stamp;
		private String name;
		private RavelCore.RavelPriority priority;

		protected void init(RavelCore.RavelPriority priority, String name, int cid, int stamp){
			this.name = name;
			this.cid = cid;
			this.stamp = stamp;
			this.priority = priority;
		}

        protected final void sleep(){
            sleep(1000);
        }

        protected final void sleep(long milis){
            try{
                Thread.sleep(milis);
            }catch (InterruptedException e){}
        }

		public abstract void run();

		public RavelCore.RavelPriority priority() {
			return this.priority;
		}

		public String name() {
			return this.name;
		}

		public int cid(){
			return cid;
		}

		public int stamp(){
			return stamp;
		}
	}

	public static class CozyTaskResult implements IName, RavelCore.RavelWorkResult{

		private RavelCore.RavelWorkResult result;
		private CozyTask task;
		private Exception exception;

		public CozyTaskResult(RavelCore.RavelWorkResult workResult){
			this.result = workResult;
			task = (CozyTask)workResult.task();
			exception = workResult.exception();
		}

		public String name() {
			return task.name();
		}

		public CozyTask task() {
			return task;
		}

		public Exception exception() {
			return exception;
		}
	}

}
