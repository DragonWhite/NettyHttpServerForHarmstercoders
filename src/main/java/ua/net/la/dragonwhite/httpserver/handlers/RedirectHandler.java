package ua.net.la.dragonwhite.httpserver.handlers;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.net.InetSocketAddress;
import java.util.List;

import ua.net.la.dragonwhite.httpserver.statistics.Statistics;
import ua.net.la.dragonwhite.httpserver.tools.Mapped;
import ua.net.la.dragonwhite.httpserver.tools.ResponseHandler;

@Mapped(uri = "/redirect")
public class RedirectHandler extends ResponseHandler {

	public RedirectHandler(InetSocketAddress remoteAddress, HttpRequest request) {
		super(remoteAddress, request);
		buffer.append("<head><tr>Nope <p> 404 Not Found</tr></head>");
	}

	@Override
	public FullHttpResponse createResponse(boolean bGoodRequest) {
		QueryStringDecoder qsd = new QueryStringDecoder(request.getUri());
		List<String> redirectUrl = qsd.parameters().get("url");
		if (redirectUrl == null) {
			uri = null;
			FullHttpResponse f = super.createResponse(false);
			return f;
		}
		
		uri = redirectUrl.get(0);
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				HttpResponseStatus.FOUND);
		response.headers().set(HttpHeaders.Names.LOCATION, uri);
		
		return response;
	}
	
	@Override
	public String getContentType() {
		return "text/html; charset=UTF-8";
	}
	
	@Override
	public void log(long read_byres, long send_bytes, long speed) {
		Statistics.addRedirect(uri);
		super.log(read_byres, send_bytes, speed);
	}
}
