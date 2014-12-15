package ua.net.la.dragonwhite.httpserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer {
	private int port;

	public HttpServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap boot = new ServerBootstrap();
			boot.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new ServerInitializer());
			System.out.println("Starting server on port "+port);
			ChannelFuture cf = boot.bind(port).sync();
			cf.channel().closeFuture().sync();
			
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
