package com.mserpa.netty.poc;

import java.net.InetAddress;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;

@Sharable
public class MyHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
		if (msg instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) msg;
			
			System.out.println(req.uri());
			if(req.uri().contains("blockingtask")) {
				String[] splits = req.uri().split("/");
				System.out.println(String.format("[%s] - %s", splits[2], "Starting blocking Task "));
				Thread.sleep(10 * 1000);
				System.out.println(String.format("[%s] - Running in thread: %s", splits[2], Thread.currentThread().getName()));
				System.out.println(String.format("[%s] - %s", splits[2], "Finishing blocking Task "));
			}
			System.out.println("Running normal code");
			boolean keepAlive = HttpUtil.isKeepAlive(req);

			String hostAddress = InetAddress.getLocalHost().getHostAddress();

			FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
					Unpooled.wrappedBuffer(hostAddress.getBytes()));
			response.headers().set(CONTENT_TYPE, TEXT_PLAIN).setInt(CONTENT_LENGTH, response.content().readableBytes());

			if (keepAlive) {
				if (!req.protocolVersion().isKeepAliveDefault()) {
					response.headers().set(CONNECTION, KEEP_ALIVE);
				}
			} else {

				response.headers().set(CONNECTION, CLOSE);
			}

			ChannelFuture f = ctx.write(response);

			if (!keepAlive) {
				f.addListener(ChannelFutureListener.CLOSE);
			}
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
