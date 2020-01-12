package com.marceloserpa.netty.transportation;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyNioServer {

	public static void main(String[] args) throws InterruptedException {
		NettyNioServer nettyOioServer = new NettyNioServer();
		nettyOioServer.serve(9090);
	}
		
	public void serve(int port) throws InterruptedException {
		final ByteBuf buf = Unpooled.unreleasableBuffer(
				Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
		EventLoopGroup group = new NioEventLoopGroup();
		
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(group)
			// Specifies the use o OIO for network transport
			.channel(NioServerSocketChannel.class)
			.localAddress(new InetSocketAddress(port))			
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(
							new ChannelInboundHandlerAdapter() {
								public void channelActive(ChannelHandlerContext ctx) {
									ctx.writeAndFlush(buf.duplicate())
										.addListener(ChannelFutureListener.CLOSE);
								}
							});
				}

			
			});
		ChannelFuture channelFuture;
		try {
			channelFuture = serverBootstrap.bind().sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			// Shutdown eventloop and release all resources
			group.shutdownGracefully().sync();
		}
	}
	
}
