package mina.client;

import java.net.InetSocketAddress;

import mina.util.ByteArray;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.alibaba.fastjson.JSONObject;

public class TestClient {
	public static void main(String[] args) { 
	// 创建客户端连接器. 
		for(int i = 0; i < 10; i++){
			new Work();
		}
//	NioSocketConnector connector = new NioSocketConnector(); 
//	connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
//	connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new MessageCodecFactory())); //设置编码过滤器 
//	connector.setConnectTimeoutMillis(30000); 
//	connector.setHandler(new ClientMessageHandler());//设置事件处理器 
//	ConnectFuture cf = connector.connect( 
//	new InetSocketAddress("127.0.0.1", 50000));//建立连接 
//	cf.awaitUninterruptibly();//等待连接创建完成 
//	ByteArray ba = new ByteArray();
//	ba.writeShort(1);
//	ba.writeShort(1);
//	ba.writeUTF("涂鸦");
//	cf.getSession().write(ba.toArray());
////	cf.getSession().write("涂鸦");//发送消息 
////	cf.getSession().write("quit");//发送消息 
//	cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
//	connector.dispose(); 
	} 
	
	public static class Work{
		public Work(){
			NioSocketConnector connector = new NioSocketConnector(); 
			connector.getFilterChain().addLast( "logger", new LoggingFilter() ); 
			connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new MessageCodecFactory())); //设置编码过滤器 
			connector.setConnectTimeoutMillis(30000); 
			connector.setHandler(new ClientMessageHandler());//设置事件处理器 
			ConnectFuture cf = connector.connect( 
					new InetSocketAddress("127.0.0.1", 50000));//建立连接 
			cf.awaitUninterruptibly();//等待连接创建完成 
			ByteArray ba = new ByteArray();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("parseId", 1);
			jsonObject.put("command", 1);
			jsonObject.put("text", "涂鸦");
			ba.writeUTF(jsonObject.toJSONString());
			cf.getSession().write(ba.toArray());
//			cf.getSession().write("涂鸦");//发送消息 
//			cf.getSession().write("quit");//发送消息 
//			cf.getSession().getCloseFuture().awaitUninterruptibly();//等待连接断开 
//			connector.dispose();
		}
		
	}
}
