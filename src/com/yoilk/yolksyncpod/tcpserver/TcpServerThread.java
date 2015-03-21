package com.yoilk.yolksyncpod.tcpserver;

import com.yoilk.yolksyncpod.gui.PodGui;

public class TcpServerThread implements Runnable {
	private TcpServerPool tsp0=null;
	private PodGui pg;
	public TcpServerThread(TcpServerPool tsp,PodGui pg0){
		tsp0=tsp;
		pg=pg0;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = 7070;
		System.out.println("Tcp Server Runs on localhost:"+port);
		try {
			new TcpServer(tsp0,pg).bind(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
