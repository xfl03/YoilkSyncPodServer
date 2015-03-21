package com.yoilk.yolksyncpod.udpserver;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

public class UdpServerHandler extends SimpleChannelInboundHandler<String> {
	//private static final String CR = System.getProperty("line.separator");
	private static final String CR=".MESSAGE_END.";
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg)
			throws Exception {
		File file = new File(msg);
		System.out.println("Udp got a request: "+msg);
		if (file.exists()) {
		    if (!file.isFile()) {
		    	ctx.writeAndFlush("Not a file : " + file + CR);
		    	System.out.println(msg+" not a file");
		    	return;
		    }
		    if(!file.getName().endsWith(".mp3")
		    		&&!file.getName().endsWith(".wav")){
		    	ctx.writeAndFlush("Access Forbidden : " + file + CR);
		    	System.out.println(msg+" access forbidden");
		    	return;
		    }
		    //ctx.write(file + " " + file.length() + CR);
		    System.out.println(msg+" begin");
		    RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
		    FileRegion region = new DefaultFileRegion(
			    randomAccessFile.getChannel(), 0, randomAccessFile.length());
		    ctx.write(region);
		    ctx.writeAndFlush(CR);
		    randomAccessFile.close();
		    System.out.println(msg+" end");
		} else {
		    ctx.writeAndFlush("File not found: " + file + CR);
		    System.out.println(msg+" not found");
		}
	}
}
