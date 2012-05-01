package ramshteks.java.custom_supadupa.logic;

import ramshteks.java.supadupa.core.ClientCore;
import ramshteks.java.supadupa.core.Core;

public class BaseClientLogic implements ClientCore.ClientLogicListener{
	private ClientCore.IClientAgent agent;
	private Core.ILogger logger;


	public final void initLogic(ClientCore.IClientAgent agent, Core.ILogger logger) {
		this.agent = agent;
		this.logger = logger;
	}

	public void onStartLogic() {
		log().log("start logic");
	}

	public void onPacketReceived(Core.IPacket packet) {
		log().log("packet received");
	}

	public void onStopLogic() {
		log().log("stop logic");
	}

	protected final Core.ILogger log(){
		return logger;
	}

	protected final ClientCore.IClientAgent agent(){
		return agent;
	}
}
