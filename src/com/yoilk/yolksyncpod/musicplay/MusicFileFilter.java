package com.yoilk.yolksyncpod.musicplay;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class MusicFileFilter extends FileFilter {

	@Override
	public boolean accept(File arg0) {
		// TODO Auto-generated method stub
		return arg0.getName().endsWith(".wav")
				||arg0.getName().endsWith(".mp3")
				||arg0.isDirectory();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Music File(*.mp3,*.wav)";
	}

}
