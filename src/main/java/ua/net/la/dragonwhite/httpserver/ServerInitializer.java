package ua.net.la.dragonwhite.httpserver;

import ua.net.la.dragonwhite.httpserver.handlers.HttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.traffic.AbstractTrafficShapingHandler;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		TrafficRecorder recorder = new TrafficRecorder(AbstractTrafficShapingHandler.DEFAULT_MAX_TIME);
		p.addLast("traffic", recorder);
		p.addLast("decoder", new HttpRequestDecoder());
		p.addLast("encoder", new HttpResponseEncoder());
		p.addLast("handler", new HttpHandler(recorder.getTrafficCounter()));
	}
}
