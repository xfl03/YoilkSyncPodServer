package com.yoilk.yolksyncpod.udpserver;

import com.yoilk.yolksyncpod.gui.PodGui;

/**
 * A thread to run udp server
 * 
 * @author Alexander Xia[xfl03] 2015
 * @since 0.9
 * */
public class UdpServerThread implements Runnable {
	
	/**
	 * Use PodGui(pg) to change GUI in order to notify user
	 * And limit user's behavior(By disabling button, etc.)
	 * It will be given to UdpServer then to UdpServerHandler
	 * 
	 * @since 0.9
	 * */
	private PodGui pg;
	
	/**
	 * Create new UdpServerThread
	 * Need PodGui to be give to UdpServer then to UdpServerHandler
	 * 
	 * @param PodGui(pg0)
	 * @since 0.9
	 * */
	public UdpServerThread(PodGui pg0){
		pg=pg0;	
	}
	
	/**
	 * Main method
	 * Use .start() to active it
	 * 
	 * @since 0.9
	 * */
	@Override
	public void run() {
		int port = 9090;
		try {
			System.out.println("Udp Server Runs on localhost:"+port);
			new UdpServer(pg).run(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
