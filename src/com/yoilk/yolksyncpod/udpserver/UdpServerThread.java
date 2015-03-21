package com.yoilk.yolksyncpod.udpserver;

import com.yoilk.yolksyncpod.gui.PodGui;

public class UdpServerThread implements Runnable {
	private PodGui pg;
	public UdpServerThread(PodGui pg0){
	pg=pg0;	
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = 9090;
		try {
			System.out.println("Udp Server Runs on localhost:"+port);
			new UdpServer(pg).run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
