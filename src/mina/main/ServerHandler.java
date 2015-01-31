package mina.main;

import mina.server.MessageDispatcher;
import mina.server.ReceivedMessage;
import mina.util.ByteArray;
import mina.util.ZipUtil;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ServerHandler extends IoHandlerAdapter {
	
	private Logger logger = Logger.getLogger(ServerHandler.class);
	
	private MessageDispatcher messageDispatcher;
	
	public ServerHandler(MessageDispatcher messageDispatcher) {
		this.messageDispatcher = messageDispatcher;
	}


	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		byte[] bytes = (byte[])message;
		if(bytes != null && bytes.length > 0){
			ByteArray ba = new ByteArray(bytes);
			JSONObject jsonObject = JSON.parseObject(ba.readUTF());
			ReceivedMessage receivedMessage = new ReceivedMessage(session, jsonObject);
			messageDispatcher.addReceivedMessage(receivedMessage);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.info("与" + session + " 断开了连接。");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		JSONObject reObject = new JSONObject();
		reObject.put("data", "测试");
		reObject.put("parseId", 0x0801);
		reObject.put("command", 0x0002);
		ByteArray ba = new ByteArray();
		ba.writeUTF(reObject.toJSONString());
		byte[] out = ZipUtil.zip(ba.toArray());
		for(int i = 0; i < 100; i++){
			session.write(out);
		}
		logger.info("新建立了一个连接" + session + "。");
	}


}
