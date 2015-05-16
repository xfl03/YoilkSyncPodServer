package com.yoilk.yolksyncpod.musicplay;

import java.util.TimerTask;

import com.yoilk.yolksyncpod.gui.PodGui;

public class MusicTimer extends TimerTask {
	
	private PodGui pg;
	private MusicPlay mp;
	
	public MusicTimer(PodGui pg0,MusicPlay mp0){
		pg=pg0;
		mp=mp0;
	}
	
	@Override
	public void run() {
		//Get Time
		long tt=mp.getSecondPositon();
		int ss=(int)(tt%60);
		int mm=(int)(tt/60);
		
		//Set Progress
		pg.progress.setValue((int)tt);
		pg.timeNow.setText((mm<10?""+mm:mm)+":"+(ss<10?"0"+ss:ss));
		
		if(tt==mp.getSecondLength()){ //Music End
			pg.timeNow.setText("0:00");
			pg.progress.setValue(0);
			pg.playing=false;
			pg.playMusic.setText("Play");
			pg.t0.cancel();
		}
		
	}

}
