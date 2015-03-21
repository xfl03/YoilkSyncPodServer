package com.yoilk.yolksyncpod.musicplay;

import java.io.File;

import com.yoilk.yolksyncpod.gui.PodGui;

public class MusicInitThread implements Runnable {
	private String name;
	private MusicPlay mp;
	private PodGui pg;
	public MusicInitThread(MusicPlay mp0,String name0,PodGui pg0){
		mp=mp0;
		name=name0;
		pg=pg0;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Begin to load music");
		long t1=System.currentTimeMillis();
		mp.load(new File(name));
		System.out.println("Finish loading music "
		+(System.currentTimeMillis()-t1));
		if(mp.clip2==null){
			pg.musicNow.setText("No Opened File");
			pg.noti.setText("File Cannot Be Loaded");
			pg.openMusic.setEnabled(true);
		}else{
			pg.noti.setText("File Loaded");
			long tt=mp.getSecondLength();
			int ss=(int)(tt%60);
			int mm=(int)(tt/60);
			System.out.println(tt);
			pg.timeAll.setText((mm<10?""+mm:mm)+":"+(ss<10?"0"+ss:ss));
			pg.progress.setMaximum((int) tt);
			pg.progress.setEnabled(true);
			pg.openMusic.setEnabled(true);
			pg.playMusic.setEnabled(true);
			pg.stopMusic.setEnabled(true);
			pg.resendMusic.setEnabled(true);
		}
	}

}
