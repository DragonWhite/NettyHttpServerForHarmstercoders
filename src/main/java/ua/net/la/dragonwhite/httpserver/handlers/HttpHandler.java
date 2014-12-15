package ua.net.la.dragonwhite.httpserver.handlers;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.traffic.TrafficCounter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ua.net.la.dragonwhite.httpserver.statistics.Statistics;
import ua.net.la.dragonwhite.httpserver.tools.Mapped;
import ua.net.la.dragonwhite.httpserver.tools.ReflectionTools;
import ua.net.la.dragonwhite.httpserver.tools.ResponseHandler;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class HttpHandler extends ChannelInboundHandlerAdapter {

	private static Map<String, Class> handlers = new HashMap<String, Class>();
	private static DefaultHandler defaultHandler = new DefaultHandler(null,
			null);
	private ResponseHandler handler;
	private TrafficCounter trafficCounter;

	static {
		try {
			for (Class c : ReflectionTools.getClasses(HttpHandler.class
					.getPackage().getName())) {
				Annotation annotation = c.getAnnotation(Mapped.class);
				if (annotation != null) {
					handlers.put(((Mapped) annotation).uri(), c);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HttpHandler(TrafficCounter traffic) {
		trafficCounter = traffic;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		Statistics.addActiveCount();
		defaultHandler.setRemoteAddress((InetSocketAddress) ctx.channel()
				.remoteAddress());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		Statistics.removeActiveCount();
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private boolean bIsKeepAlive = false;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object message) {
		if (message instanceof HttpRequest) {
			HttpRequest request = (HttpRequest) message;
			bIsKeepAlive = isKeepAlive(request);
			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
					request.getUri());
			String path = queryStringDecoder.path();
			Class classHandler = handlers.get(path);
			if (classHandler != null) {
				InetSocketAddress iSocket = (InetSocketAddress) ctx.channel()
						.remoteAddress();
				handler = ResponseHandler.createResponseHandler(classHandler,
						iSocket, request);
			}
		}

		if (message instanceof LastHttpContent) {
			FullHttpResponse response;
			boolean bSuccess = ((LastHttpContent) message).getDecoderResult()
					.isSuccess();
			if (handler == null) {
				response = defaultHandler.createResponse(bSuccess);
			} else {
				response = handler.createResponse(bSuccess);
			}

			if (!bIsKeepAlive
					|| response.getStatus() == HttpResponseStatus.FOUND) {
				ctx.write(response).addListener(ChannelFutureListener.CLOSE);
			} else {
				response.headers().set(CONNECTION, Values.KEEP_ALIVE);
				ctx.write(response);
			}

			if (handler != null)
				logQuery();

			handler = null;
			bIsKeepAlive = false;
		}
	}

	private void logQuery() {
		long read = trafficCounter.cumulativeReadBytes();
		long write = trafficCounter.cumulativeWrittenBytes();
		long time = new Date().getTime() - trafficCounter.lastCumulativeTime();
		long speed = time == 0 ? 0 : write * 1000 / time;
		handler.log(read, write, speed);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		if (!(cause instanceof IOException))
			cause.printStackTrace();
		else {
			// TODO IOException brother exit
		}
		ctx.close();
	}

}
