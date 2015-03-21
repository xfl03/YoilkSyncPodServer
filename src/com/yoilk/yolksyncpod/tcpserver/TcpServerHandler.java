package com.yoilk.yolksyncpod.tcpserver;

import java.net.InetSocketAddress;

import com.yoilk.yolksyncpod.gui.PodGui;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
public class TcpServerHandler extends ChannelInboundHandlerAdapter {
	private String ClientID=null;
	private boolean auth=false;
	private ChannelHandlerContext ctx0=null;
	private TcpServerPool tsp0=null;
	private PodGui pg;
	public TcpServerHandler(TcpServerPool tsp,PodGui pg0){
		tsp0=tsp;
		pg=pg0;
	}
	public void printConnectNumber(){
		pg.connect.setText("Connected:"+tsp0.a.size());
	}
	@Override
    public void channelActive(ChannelHandlerContext ctx) {
		ctx0=ctx;
		ClientID=System.currentTimeMillis()+" "+
				((InetSocketAddress) ctx.channel().remoteAddress())
				.getAddress().getHostAddress();
		tsp0.a.put(ClientID, this);
		System.out.println(ClientID+" Try To Connect "+tsp0.a.size());
		pg.noti.setText(ClientID+" Try To Connect");
		printConnectNumber();
	}
	@Override
    public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println(ClientID+" Disconnected");
		tsp0.a.remove(ClientID);
		printConnectNumber();
		pg.noti.setText(ClientID+" Disconnected");
	}
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		System.out.println("Server receive request : " + body+" FROM "+ClientID );
		if(!auth){
			if(body.startsWith("YSPC")){
				if(body.equalsIgnoreCase("YSPC 0.9")){
					auth=true;
					write(ctx,"AUTH SUCCESS");
					System.out.println(ClientID+" Auth Success");
					pg.noti.setText(ClientID+" Auth Success");
				}else{
					write(ctx,"CLIENT OUT OF DATE");
					System.out.println(ClientID+" Client Out Of Date");
					pg.noti.setText(ClientID+" Client Out Of Date");
					ctx.close();
				}
			}else{
				write(ctx,"BAD CONNECT");
				System.out.println(ClientID+" Bad Connect");
				ctx.close();
				pg.noti.setText(ClientID+" Bad Connect");
			}
		}else{
			//write(ctx,"");
			if(body.startsWith("CA ")){//Client Answer [CA command answer]
				String[] requ=body.split(" ");
				String[] reque={"",""};
				if (requ.length>=3){
					reque[0]=requ[1];
					reque[1]=body.replaceFirst("CA "+requ[1], "");
				}else if(requ.length==2){
					reque[0]=requ[1];
				}
				pg.noti.setText(ClientID+" : "+body);
			}
		}
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	System.out.println(cause.getMessage());
    	ctx.close();
    }
    private void write(ChannelHandlerContext ctx,String text){
    	text=text+System.getProperty("line.separator");
    	ByteBuf resp = Unpooled.copiedBuffer(text.getBytes());
    	ctx.writeAndFlush(resp);
    }
    public void write(String text){
    	text=text+System.getProperty("line.separator");
    	ByteBuf resp = Unpooled.copiedBuffer(text.getBytes());
    	if(ctx0!=null)
    		ctx0.writeAndFlush(resp);
    	else
    		System.out.println("ctx0==null");
    }
}
