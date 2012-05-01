package ramshteks.java.cozy;

import ramshteks.java.cozy.core.CozyClientCore;
import ramshteks.java.custom_supadupa.logic.BaseClientLogic;
import ramshteks.java.patterns.Strategy;
import ramshteks.java.supadupa.core.Core;
import ramshteks.java.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.util.Hashtable;

public class CozyClient extends BaseClientLogic {
	private Strategy listeners;
	private Hashtable<Short, CozyClientCore.IClientParser> parsers;

	public CozyClient(){
		listeners = new Strategy();
		parsers = new Hashtable<Short, CozyClientCore.IClientParser>();
	}

	protected final void addParser(CozyClientCore.IClientParser parser, Strategy.Method listener){
		listeners.addHandler(parser.name(), listener);
		parsers.put(parser.commandCode(), parser);
	}

	private void execute(Core.IPacket packet) throws NullPointerException{

		CozyClientCore.IClientParser parser;
		CozyClientCore.Response response;

		byte[] bytes = packet.bytes();

		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		short code = buffer.getShort();//ByteUtils.getShort(bytes);

		if(parsers.containsKey(code)){

			ByteBuffer byteBuffer = ByteBuffer.wrap(ByteUtils.getSubArray(bytes, 2));
			parser = parsers.get(code);
			response = parser.parse(byteBuffer);

			if(response==null)throw new NullPointerException("Parser return null-packet");

			listeners.execute(response);
		}else{
			log().log("No parser for code="+code);
		}
	}

	protected final void send(CozyClientCore.Request request){
		agent().send(request);
	}

	@Override
	public void onStartLogic() {
		super.onStartLogic();
	}

	@Override
	public final void onPacketReceived(Core.IPacket packet) {
		//super.onPacketReceived(packet);
		execute(packet);
	}

	@Override
	public void onStopLogic() {
		super.onStopLogic();
	}
}
