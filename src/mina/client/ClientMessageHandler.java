package mina.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ClientMessageHandler extends IoHandlerAdapter {

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		byte[] bytes = (byte[])message;
		System.out.println(bytes);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("连接关闭： " + session);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("连接建立： " + session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("连接打开： " + session);
	}
	
	

}
