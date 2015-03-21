package com.yoilk.yolksyncpod.musicplay;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class MusicPlay {
	private AudioInputStream stream;
	public Clip clip2;
	private AudioFormat format;
	public void load(File music){
		try {
			System.out.println("runs1");
			stream = AudioSystem.getAudioInputStream(music);
			format = stream.getFormat();
	        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
	            format = new AudioFormat(
	              AudioFormat.Encoding.PCM_SIGNED,
	              format.getSampleRate(),
	              16,
	              format.getChannels(),
	              format.getChannels() * 2,
	              format.getSampleRate(),
	               false);        // big endian
	            stream = AudioSystem.getAudioInputStream(format, stream);
	        }
	        System.out.println("run2");
			clip2 = AudioSystem.getClip();
	        clip2.open(stream);
	        clip2.setMicrosecondPosition(0);
	        System.out.println("320");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void setPosition(long t){
		clip2.setMicrosecondPosition(t);
	}
	public void setSecondPosition(long t){
		clip2.setMicrosecondPosition(t*1000000);
	}
	public long getPosition(){
		return clip2.getMicrosecondPosition();
	}
	public long getSecondPositon(){
		return (long)((clip2.getMicrosecondPosition()+500000)/1000000);
	}
	public long getLength(){
		return clip2.getMicrosecondLength();
	}
	public long getSecondLength(){
		return (long)((clip2.getMicrosecondLength()+500000)/1000000);
	}
	public void pause(){
		clip2.stop();
	}
	public void start(){
		clip2.start();
	}
	public void close(){
		clip2.close();
		try {
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
