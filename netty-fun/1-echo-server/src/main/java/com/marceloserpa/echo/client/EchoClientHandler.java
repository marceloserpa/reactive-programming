package com.marceloserpa.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//When notified	that the channel is active, sends a	message
		ctx.writeAndFlush(Unpooled.copiedBuffer("NETTY", CharsetUtil.UTF_8));
	}

	/*
	Even for such a small amount of data, the channelRead0() method could be
	called twice, first with a ByteBuf (Netty’s byte container) holding 3 bytes, and second
	with a ByteBuf holding 2 bytes. As a stream-oriented protocol, TCP guarantees that
	the bytes will be received in the order in which they were sent by the server.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
