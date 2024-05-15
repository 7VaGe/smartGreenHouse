package seiot.consegna02;

import seiot.modulo_lab_2_2.msg.*;
import seiot.modulo_lab_2_2.msg.jssc.*;

public class MonitoringAgent extends Thread {

	SerialCommChannel channel;
	SmartCMView view;
	LogView logger;
	
	static final String CM_PREFIX 	=  "cm:";
	static final String LOG_PREFIX 	=  "lo:";
	static final String MSG_WELCOME 			= "we";
	static final String MSG_SLEEPING 		= "zz";
	static final String MSG_READY 			= "ok";
	static final String MSG_MAKING_COFFEE 	= "mc";
	static final String MSG_COFFEE_READY   	= "re";
	static final String MSG_SUGAR_LEVEL   	= "s|";
	static final String MSG_REFILLED   		= "f|";
	static final String MSG_MAINTENANCE  	= "ma";
	
	public MonitoringAgent(SerialCommChannel channel, SmartCMView view, LogView log) throws Exception {
		this.view = view;
		this.logger = log;
		this.channel = channel;
	}
	
	public void run(){
		while (true){
			try {
				String msg = channel.receiveMsg();
				if (msg.startsWith(CM_PREFIX)){
					String cmd = msg.substring(CM_PREFIX.length()); 
					logger.log("new command: "+cmd);				
					if (cmd.startsWith(MSG_WELCOME)){
						view.setInfo("Welcome!");
					} else if (cmd.startsWith(MSG_SLEEPING)){
						view.setInfo("zzZZzz");
					} else if (cmd.startsWith(MSG_READY)){
						view.setInfo("Ready");
					} else if (cmd.startsWith(MSG_MAKING_COFFEE)) {
						view.setInfo("Making a coffee");
					} else if (cmd.startsWith(MSG_COFFEE_READY)) {
						view.setInfo("The coffee is ready");
					} else if (cmd.startsWith(MSG_SUGAR_LEVEL)) {
						view.updateSugarLevel(Integer.parseInt(cmd.substring(2)));
					} else if (cmd.startsWith(MSG_MAINTENANCE)) {
						view.setInfo("No more coffee. Waiting for recharge");
						view.startMaintenance();
					} else if (cmd.startsWith(MSG_REFILLED)) {
						view.refilled(Integer.parseInt(cmd.substring(2)));
					}
				} else if (msg.startsWith(LOG_PREFIX)){
					this.logger.log(msg.substring(LOG_PREFIX.length()));
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
