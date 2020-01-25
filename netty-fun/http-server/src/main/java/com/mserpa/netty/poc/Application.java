package com.mserpa.netty.poc;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class Application {

	public static void main(String[] args) throws InterruptedException {
		serve(9000);
	}

	private static void serve(int port) throws InterruptedException {
		System.out.println("Starting server port " + port);
		EventLoopGroup group = new NioEventLoopGroup();
		
		final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(16);

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpServerCodec());
						ch.pipeline().addLast(new HttpServerExpectContinueHandler());
						
						// This code will run inside event loop thread
						//ch.pipeline().addLast(new MyHandler());
						
						// This code will run in a different threaad pool
						ch.pipeline().addLast(eventExecutorGroup, new MyHandler());
					}

				});
		ChannelFuture channelFuture;
		try {
			channelFuture = serverBootstrap.bind().sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

}
