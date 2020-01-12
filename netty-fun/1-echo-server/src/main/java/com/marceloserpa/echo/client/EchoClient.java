package com.marceloserpa.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

	public static void main(String[] args) throws InterruptedException {
		new EchoClient().start();
	}
	
	public void start() throws InterruptedException {
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(eventLoopGroup)
				.channel(NioSocketChannel.class)
				.remoteAddress("localhost", 9000)
				.handler(new ChannelInitializer<Channel>() {
	
					@Override
					protected void initChannel(Channel ch) throws Exception {
						// TODO Auto-generated method stub
						ch.pipeline().addLast(new EchoClientHandler());
					}
				});
			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();
		}finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
	
}
