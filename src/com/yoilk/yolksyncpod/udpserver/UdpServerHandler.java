package com.yoilk.yolksyncpod.udpserver;

import java.io.File;
import java.io.FileInputStream;

import com.yoilk.yolksyncpod.gui.PodGui;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class UdpServerHandler extends
	SimpleChannelInboundHandler<DatagramPacket> {
	private PodGui pg;

    public UdpServerHandler(PodGui pg0) {
		// TODO Auto-generated constructor stub
    	pg=pg0;
    }

	@Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
	    throws Exception {
	String req = packet.content().toString(CharsetUtil.UTF_8);
	System.out.println("Udp Server Recieved a request: "+req);
	pg.noti.setText("Sending "+req);
	File ff=new File(req);
	if(ff.exists()){
		if(ff.isFile()){
			if(ff.getName().endsWith(".mp3")||ff.getName().endsWith(".mp3")){
				 FileInputStream fis = new FileInputStream(ff);
				 int fileLen = fis.available();
				 byte[] buf = new byte[1024];
                 int numofBlock = fileLen / buf.length;
                 int lastSize = fileLen % buf.length;
                 DatagramPacket packet0;
                 for (int i = 0; i < numofBlock; i++) {
                     fis.read(buf, 0, buf.length);
                     packet0 = new DatagramPacket(Unpooled.copiedBuffer(buf),packet.sender());
                     ctx.writeAndFlush(packet0);
                     Thread.sleep(1);
                 }
                 buf = new byte[lastSize];
                 fis.read(buf, 0,lastSize);
                 packet0 = new DatagramPacket(Unpooled.copiedBuffer(buf),packet.sender());
                 ctx.writeAndFlush(packet0);
                 Thread.sleep(1);
                 fis.close();
                 pg.noti.setText("Sent "+req);
                 System.out.println(req+" = 1024 * "+numofBlock+" + "+lastSize+" {"+(lastSize==0?numofBlock:numofBlock+1)+")");
			}else{
				ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
					    "Access Forbidden", CharsetUtil.UTF_8), packet
					    .sender()));
			}
		}else{
			ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
				    "Not A File", CharsetUtil.UTF_8), packet
				    .sender()));
		}
	}else{
		ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer(
			    "File Not Exists", CharsetUtil.UTF_8), packet
			    .sender()));
	}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
	ctx.close();
	cause.printStackTrace();
    }
}

