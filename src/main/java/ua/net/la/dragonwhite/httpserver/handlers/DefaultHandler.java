package ua.net.la.dragonwhite.httpserver.handlers;

import java.net.InetSocketAddress;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import ua.net.la.dragonwhite.httpserver.tools.ResponseHandler;

public class DefaultHandler extends ResponseHandler{
	public DefaultHandler(InetSocketAddress remoteAddress, HttpRequest request) {
		super(remoteAddress, request);
		buffer.append("<head><tr>Nope <p> 404 Not Found</tr></head>");
	}
	@Override
	public FullHttpResponse createResponse(boolean bGoodRequest) {

		return super.createResponse(bGoodRequest);
	}
	
	@Override
	public String getContentType() {
		return "text/html; charset=UTF-8";
	}
	
	public void setRemoteAddress(InetSocketAddress remoteAddress){
		this.remoteAddress = remoteAddress;
	}
	
	@Override
	public void log(long read_bytes, long send_bytes, long speed) {
		//do nothing - do not log errors
	}
}
