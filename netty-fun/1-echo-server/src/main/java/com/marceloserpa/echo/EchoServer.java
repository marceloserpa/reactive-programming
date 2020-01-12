package com.marceloserpa.echo;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

	private static final int PORT = 9000;
	
	
	public static void main(String[] args) throws Exception {
		EchoServer echoServer = new EchoServer();
		echoServer.start();
	}
	
	public void start() throws Exception{
		final EchoServerHandler echoServerHandler = new EchoServerHandler();
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(eventLoopGroup)
			// Specifies the use o NIO for network transport
			.channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(PORT))
			
			// EchoServerHandler is @sharable so we can use the same instance here
			.childHandler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(echoServerHandler);
				}
			});
		ChannelFuture channelFuture;
		try {
			channelFuture = serverBootstrap.bind().sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			// Shutdown eventloop and release all resources
			eventLoopGroup.shutdownGracefully().sync();
		}
		
		
		
		
	}
	
}
