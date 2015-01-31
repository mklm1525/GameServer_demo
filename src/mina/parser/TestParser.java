package mina.parser;

import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import mina.server.MessageParser;
import mina.server.ReceivedMessage;
import mina.util.ByteArray;

public class TestParser extends MessageParser{
	
	public static final int C_TEST_PARSER_TEST = 0x0001;

	@Override
	public int getParseId() {
		return MessageParser.TEST_PARSER;
	}

	@Override
	public boolean parse(ReceivedMessage message) throws Exception {
		IoSession session = message.getSession();
//		Player player = session.getAttribute("player");
		JSONObject jsonObject = message.getJsonObject();
		switch(jsonObject.getInteger("command")){
		case C_TEST_PARSER_TEST:{
			String str = jsonObject.getString("text");
			System.out.println(str);
			break;
		}
		default :{
			break;
		}
		}
		return false;
	}

	@Override
	public void catchException(ReceivedMessage message, Exception e) {
		IoSession session = message.getSession();
//		session.write("操作失败");
	}
	
}