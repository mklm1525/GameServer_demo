package com.mina.httpserver;

public class HttpHandlerTest implements HttpHandler {

	@Override
	public HttpResponseMessage handle(HttpRequestMessage request) {
		String level = request.getParameter("level");
		System.out.println(request.getParameter("level"));
		System.out.println(request.getContext());
		HttpResponseMessage response = new HttpResponseMessage();
		response.setContentType("text/plain");
		response.setResponseCode(HttpResponseMessage.HTTP_STATUS_SUCCESS);
		response.appendBody("CONNECTED\n");
		response.appendBody(level);
		return response;
	}

}
