package com.yoilk.yolksyncpod.tcpserver;

import com.yoilk.yolksyncpod.gui.PodGui;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TcpServer {
	private TcpServerPool tsp0=null;
	private PodGui pg;
	public TcpServer(TcpServerPool tsp,PodGui pg0){
		tsp0=tsp;
		pg=pg0;
	}
	public void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
		    ServerBootstrap b = new ServerBootstrap();
		    b.group(bossGroup, workerGroup)
			    .channel(NioServerSocketChannel.class)
			    .option(ChannelOption.SO_BACKLOG, 1024)
			    .childHandler(new ChildChannelHandler());
		    ChannelFuture f = b.bind(port).sync();

		    f.channel().closeFuture().sync();
		} finally {
		    bossGroup.shutdownGracefully();
		    workerGroup.shutdownGracefully();
		}
	}

	private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
		    arg0.pipeline().addLast(new LineBasedFrameDecoder(1024));
		    arg0.pipeline().addLast(new StringDecoder());
		    arg0.pipeline().addLast(new TcpServerHandler(TcpServer.this.tsp0,TcpServer.this.pg));
		}
	}
}
