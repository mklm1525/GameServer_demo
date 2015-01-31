package mina.main;

import java.net.ServerSocket;
import java.net.Socket;

import mina.util.ServerConfig;

public class ShutDownServer implements Runnable {

	private static final String LOCAL = "127.0.0.1";

	private ServerSocket shutdownServer;
	private Socket connectSocket;

	public ShutDownServer() {
		try {
			shutdownServer = new ServerSocket(getPort());
			Thread t = new Thread(this);
			t.setDaemon(true);
			t.start();
		} catch (Exception e) {
		}
	}

	private int getPort() {
		return ServerConfig.getProperties().getInteger("Port") + 2;
	}

	@Override
	public void run() {
		while (true) {
			try {
				connectSocket = shutdownServer.accept();
				if (connectSocket != null) {
					String ip = connectSocket.getLocalAddress().getHostAddress();
					if (ip.equals(LOCAL)) {
						Server.server.shutDownServer();
					}
				}
				connectSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
