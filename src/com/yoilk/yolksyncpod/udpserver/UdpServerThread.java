package com.yoilk.yolksyncpod.udpserver;

public class UdpServerThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int port = 9090;
		try {
			System.out.println("Udp Server Runs on localhost:"+port);
			new UdpServer().run(port);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
