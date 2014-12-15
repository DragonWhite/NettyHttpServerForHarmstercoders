package ua.net.la.dragonwhite.httpserver.tools;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Date;

import ua.net.la.dragonwhite.httpserver.statistics.Query;
import ua.net.la.dragonwhite.httpserver.statistics.Statistics;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

public abstract class ResponseHandler {

	protected final StringBuilder buffer = new StringBuilder();
	protected HttpRequest request;
	protected String uri;
	protected InetSocketAddress remoteAddress;

	public ResponseHandler(InetSocketAddress remoteAddress, HttpRequest request) {
		this.remoteAddress = remoteAddress;
		this.request = request;
		if (request != null)
			uri = new QueryStringDecoder(request.getUri()).path();
		else
			uri = null;
	}

	public String getBuffer() {
		return buffer.toString();
	}

	public FullHttpResponse createResponse(boolean bGoodRequest) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
				bGoodRequest ? OK : BAD_REQUEST, Unpooled.copiedBuffer(
						buffer.toString(), CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, getContentType());
		response.headers().set(CONTENT_LENGTH,
				response.content().readableBytes());
		return response;
	}

	public String getContentType() {
		return "text/html; charset=UTF-8";
	}

	public void log(long read_bytes, long send_bytes, long speed) {
		Statistics.addCount();
		Statistics.addIpCounter(remoteAddress);
		Statistics.addUnique(uri, remoteAddress);
		Statistics.addQuery(new Query(remoteAddress, uri, new Date(),
				send_bytes, read_bytes, speed));
	}

	public void setRequest(HttpRequest request) {
		this.request = request;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseHandler createResponseHandler(Class handler,
			InetSocketAddress remoteAddress, HttpRequest request) {
		Class[] cArg = new Class[2];
		cArg[0] = InetSocketAddress.class;
		cArg[1] = HttpRequest.class;
		ResponseHandler response = null;
		try {
			response = (ResponseHandler) handler.getDeclaredConstructor(cArg)
					.newInstance(remoteAddress, request);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		return response;
	}

}
