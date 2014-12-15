package ua.net.la.dragonwhite.httpserver.handlers;

import java.net.InetSocketAddress;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import ua.net.la.dragonwhite.httpserver.tools.Mapped;
import ua.net.la.dragonwhite.httpserver.tools.ResponseHandler;

@Mapped(uri = "/hello")
public class HelloHandler extends ResponseHandler {
	public HelloHandler(InetSocketAddress remoteAddress, HttpRequest request) {
		super(remoteAddress, request);
		buffer.append("Hello World!\n");
	}

	@Override
	public FullHttpResponse createResponse(boolean bGoodRequest) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return super.createResponse(bGoodRequest);
	}
}
