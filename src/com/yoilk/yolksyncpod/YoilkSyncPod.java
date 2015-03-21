package com.yoilk.yolksyncpod;

import com.yoilk.yolksyncpod.gui.PodGui;
import com.yoilk.yolksyncpod.tcpserver.TcpServerPool;
import com.yoilk.yolksyncpod.tcpserver.TcpServerThread;
import com.yoilk.yolksyncpod.udpserver.UdpServerThread;


public class YoilkSyncPod {
	public static final String VERSION="0.9";
	public static void main(String[] args) {
		System.out.println("Main Thread Begins");
		// TODO Auto-generated method stub
		
		TcpServerPool tsp=new TcpServerPool();
		PodGui pg=new PodGui(tsp);
		pg.init();
		TcpServerThread tst1=new TcpServerThread(tsp,pg);
		Thread tst1Thread=new Thread(tst1);
		tst1Thread.setName("TCP Server Thread");
		tst1Thread.start();
		UdpServerThread ust1=new UdpServerThread(pg);
		Thread ust1Thread=new Thread(ust1);
		ust1Thread.setName("UDP Server Thread");
		ust1Thread.start();
		
		
		
		//System.out.println("main method end");
		/*
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Main Thread Wakes Up");
		if(tsp.a.size()>=1){
			String[] bb=tsp.a.keySet().toArray(new String[1]);
			System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
			tsp.a.get(bb[0]).write("Server Self Push Test");
			
		}
		System.out.println("main method end");
		*/
	}
	

}
