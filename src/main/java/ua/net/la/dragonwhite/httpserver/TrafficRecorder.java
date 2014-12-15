package ua.net.la.dragonwhite.httpserver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.traffic.ChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;

public class TrafficRecorder extends ChannelTrafficShapingHandler {

	public TrafficRecorder(long checkInterval) {
		super(checkInterval);
	}

	public TrafficCounter getTrafficCounter() {
		return trafficCounter();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		getTrafficCounter().resetCumulativeTime();
		super.channelRead(ctx, msg);
	}
}
