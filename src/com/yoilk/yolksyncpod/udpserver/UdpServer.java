package com.yoilk.yolksyncpod.udpserver;

import com.yoilk.yolksyncpod.gui.PodGui;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer {
	private PodGui pg;
	public UdpServer(PodGui pg0) {
		// TODO Auto-generated constructor stub
		pg=pg0;
	}



	public void run(int port) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
		    Bootstrap b = new Bootstrap();
		    b.group(group).channel(NioDatagramChannel.class)
			    .option(ChannelOption.SO_BROADCAST, true)
			    .handler(new UdpServerHandler(pg));
		    b.bind(port).sync().channel().closeFuture().await();
		} finally {
		    group.shutdownGracefully();
		}
	}

}
