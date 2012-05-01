package ramshteks.java.cozy.core;

import ramshteks.java.patterns.interfaces.IName;

import java.nio.ByteBuffer;

public class CozyClientCore {

	public static interface  IClientParser extends IName{
		Response parse(ByteBuffer bytes);
        short commandCode();
	}

	public static class Response extends CozyCore.Request{
		public Response(short command, String name) {
			super(command, name, -1);
		}
	}

	public static class Request extends CozyCore.Response{
		public Request(int command) {
			super(command);
		}
	}

}
