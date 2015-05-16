package com.yoilk.yolksyncpod.udpserver;

import java.io.File;
import java.io.FileInputStream;

import com.yoilk.yolksyncpod.gui.PodGui;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;
/**
 * Handler for udp server.
 * When client sends DatagramPacket or exception occurs,
 * This handler will handle it.
 * 
 * @author Alexander Xia[xfl03] 2015
 * @since 0.9
 * */
public class UdpServerHandler extends
	SimpleChannelInboundHandler<DatagramPacket> {
	
	/**
	 * Use PodGui(pg) to change GUI in order to notify user
	 * And limit user's behavior(By disabling button, etc.)
	 * 
	 * @since 0.9
	 * */
	private PodGui pg;

	/**
	 * Create new UdpServerHandler
	 * Need PodGui to use in other code
	 * 
	 * @param PodGui(pg0)
	 * @since 0.9
	 * */
    public UdpServerHandler(PodGui pg0) {
    	pg=pg0;
    }

    /**
	 * When client sends a packet,
	 * This method will be used.
	 * 
	 * @param ChannelHandlerContext,DatagramPacket(udp packet)
	 * @since 0.9
	 * */
	@Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
	    throws Exception {
		String req = packet.content().toString(CharsetUtil.UTF_8);
		System.out.println("Udp Server Recieved a request: "+req);
		
		File ff=new File(req);
		
		if(ff.exists()){
			if(ff.isFile()){
				if(ff.getName().endsWith(".mp3")||ff.getName().endsWith(".mp3")){ //Send File
					pg.noti.setText("Sending "+req);
					
					FileInputStream fis = new FileInputStream(ff);
					int fileLen = fis.available();
					byte[] buf = new byte[1024];
					int numofBlock = fileLen / buf.length;
					int lastSize = fileLen % buf.length;
					
					DatagramPacket packet0;
					
					//Send Full Blocks
					for (int i = 0; i < numofBlock; i++) {
						fis.read(buf, 0, buf.length);
						packet0 = new DatagramPacket(Unpooled.copiedBuffer(buf),packet.sender());
						ctx.writeAndFlush(packet0);
						Thread.sleep(1);//In Order Not To Lose Packet
					}
					
					//Send Last
					buf = new byte[lastSize];
					fis.read(buf, 0,lastSize);
					packet0 = new DatagramPacket(Unpooled.copiedBuffer(buf),packet.sender());
					ctx.writeAndFlush(packet0);
					Thread.sleep(1);//In Order Not To Lose Packet
					
					//End
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

	/**
	 * When exception occurs,
	 * This method will be used.
	 * 
	 * @param ChannelHandlerContext,Throwable
	 * @since 0.9
	 * */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	    throws Exception {
    	ctx.close();
    	cause.printStackTrace();
    }
    
}

