package com.mina.httpservertest;

import java.io.IOException;

import com.mina.httpserver.HttpHandler;
import com.mina.httpserver.HttpHandlerTest;
import com.mina.httpserver.HttpRequestMessage;
import com.mina.httpserver.HttpResponseMessage;
import com.mina.httpserver.HttpServer;

public class TestHttpServer {
	public static void main(String[] args) throws IOException,
			InterruptedException {
		HttpServer server = new HttpServer();
		server.setEncoding("GB2312");
		server.setHttpHandler(new HttpHandlerTest());
		server.run();

		Thread.sleep(10000);
		// server.stop();
	}
}
