package com.yoilk.yolksyncpod;

import com.yoilk.yolksyncpod.gui.PodGui;
import com.yoilk.yolksyncpod.tcpserver.TcpServerPool;
import com.yoilk.yolksyncpod.tcpserver.TcpServerThread;
import com.yoilk.yolksyncpod.udpserver.UdpServerThread;


public class YoilkSyncPod {
	
	public static final String VERSION="0.9.1";
	
	public static void main(String[] args) {
		
		//Init Tcp Pool which is used to storage Tcp client target
		TcpServerPool tsp=new TcpServerPool();
		
		//Init Main Gui
		PodGui pg=new PodGui(tsp);
		pg.init();
		
		//Init and Start Tcp Server which is used to transport command
		TcpServerThread tst1=new TcpServerThread(tsp,pg);
		Thread tst1Thread=new Thread(tst1);
		tst1Thread.setName("TCP Server Thread");
		tst1Thread.start();
		
		//Init and Start Ucp Server which is used to transport music file
		//In fact, it is better and faster to transport file in Tcp Server
		//To make a Udp Server is only for testing Udp. 
		//TODO change Udp to Tcp
		UdpServerThread ust1=new UdpServerThread(pg);
		Thread ust1Thread=new Thread(ust1);
		ust1Thread.setName("UDP Server Thread");
		ust1Thread.start();
		
	}
}
