package mina.server;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSONObject;

public class ReceivedMessage {
	
	private IoSession session;
	private JSONObject jsonObject;
	
	public ReceivedMessage(){
		
	}
	
	public ReceivedMessage(IoSession session, JSONObject jsonObject){
		this.session = session;
		this.jsonObject = jsonObject;
	}

	public IoSession getSession() {
		return session;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}
	

}
