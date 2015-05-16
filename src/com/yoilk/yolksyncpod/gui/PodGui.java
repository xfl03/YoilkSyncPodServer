package com.yoilk.yolksyncpod.gui;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.yoilk.yolksyncpod.musicplay.MusicFileFilter;
import com.yoilk.yolksyncpod.musicplay.MusicInitThread;
import com.yoilk.yolksyncpod.musicplay.MusicPlay;
import com.yoilk.yolksyncpod.musicplay.MusicTimer;
import com.yoilk.yolksyncpod.tcpserver.TcpServerPool;

public class PodGui {
	
	public JFrame jf;
	
	public Container con;
	
	public TcpServerPool tsp;
	
	public JLabel connect;
	
	public JLabel timeNow;
	
	public JLabel timeAll;
	
	public JLabel musicNow;
	
	public JSlider progress;
	
	public JButton openMusic;
	
	public JButton resendMusic;
	
	public JButton playMusic;
	
	public JButton stopMusic;
	
	public JLabel noti;
	
	public String musicFilePath=".";
	
	public MusicPlay mp1;
	
	public Timer t0;
	
	public int oldtt=0;
	
	public boolean playing=false;
	
	public PodGui(TcpServerPool tsp0) {
		tsp=tsp0;
	}
	
	public void init(){
		jf=new JFrame("Yoilk Sync Pod");
		con = jf.getContentPane();
		con.setLayout(null);
		jf.setBounds(400, 200, 400, 250);
		Font boldCourier=new Font("Courier", Font.BOLD, 30);
		Font boldCourier2=new Font("Courier", Font.BOLD, 15);
		Font boldCourier3=new Font("Courier", Font.BOLD, 18);
		Font boldCourier4=new Font("Courier", Font.BOLD, 17);
		
		JLabel title=new JLabel();
        title.setText("Yoilk Sync Pod");
        title.setFont(boldCourier);
        title.setBounds(5, 5, 385, 30);
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        con.add(title);
        
        connect=new JLabel();
        connect.setText("Connected:0");
        connect.setFont(boldCourier2);
        connect.setBounds(5, 40, 385, 12);
        connect.setHorizontalAlignment(JLabel.CENTER);
        connect.setVerticalAlignment(JLabel.CENTER);
        con.add(connect);
		
        progress=new JSlider();
        progress.setBounds(10, 75, 375, 20);
        progress.setValue(0);
        progress.setMinimum(0);
        progress.setMaximum(60);
        progress.setEnabled(false);
        con.add(progress);
        progress.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				int tt=progress.getValue();
				if(tt-oldtt>=2||tt-oldtt<=-2){
					int ss=(int)(tt%60);
					int mm=(int)(tt/60);
					if(tsp.a.size()>=1){
                		String[] bb=tsp.a.keySet().toArray(new String[1]);
                		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
                		for(String bbb : bb){
                			System.out.println("Send 'jump "+tt+"' to "+bbb);
                			noti.setText("Send 'jump "+tt+"' to "+bbb);
                			tsp.a.get(bbb).write("SC jump "+tt);
                		}
                	}
					mp1.setSecondPosition(tt);
					timeNow.setText((mm<10?""+mm:mm)+":"+(ss<10?"0"+ss:ss));
					
				}
				oldtt=tt;
			}});
        
        timeNow=new JLabel();
        timeNow.setText("00:00");
        timeNow.setFont(boldCourier2);
        timeNow.setBounds(5, 95, 50, 12);
        timeNow.setHorizontalAlignment(JLabel.LEFT);
        timeNow.setVerticalAlignment(JLabel.CENTER);
        con.add(timeNow);
        
        musicNow=new JLabel();
        musicNow.setText("No Opened File");
        musicNow.setFont(boldCourier4);
        musicNow.setBounds(55, 91, 285, 20);
        musicNow.setHorizontalAlignment(JLabel.CENTER);
        musicNow.setVerticalAlignment(JLabel.CENTER);
        con.add(musicNow);
        
        timeAll=new JLabel();
        timeAll.setText("00:00");
        timeAll.setFont(boldCourier2);
        timeAll.setBounds(340, 95, 50, 12);
        timeAll.setHorizontalAlignment(JLabel.RIGHT);
        timeAll.setVerticalAlignment(JLabel.CENTER);
        con.add(timeAll);
        
        t0=new Timer();
        
        
        playMusic=new JButton();
        playMusic.setText("Play");
        playMusic.setBounds(10, 123, 186, 30);
        playMusic.setFont(boldCourier3);
        playMusic.setEnabled(false);
        con.add(playMusic);
        playMusic.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent e){
            	if(playing){
            		playMusic.setText("Play");
            		mp1.pause();
            		playing=false;
            		t0.cancel();
            		if(tsp.a.size()>=1){
                		String[] bb=tsp.a.keySet().toArray(new String[1]);
                		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
                		for(String bbb : bb){
                			System.out.println("Send 'pause' to "+bbb);
                			noti.setText("Send 'pause' to "+bbb);
                			tsp.a.get(bbb).write("SC pause");
                		}
                	}
            	}else{
            		playMusic.setText("Pause");
            		mp1.start();
            		playing=true;
            		if(tsp.a.size()>=1){
                		String[] bb=tsp.a.keySet().toArray(new String[1]);
                		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
                		for(String bbb : bb){
                			System.out.println("Send 'play' to "+bbb);
                			noti.setText("Send 'play' to "+bbb);
                			tsp.a.get(bbb).write("SC play");
                		}
                	}
            		t0=new Timer();
            		t0.schedule(new MusicTimer(PodGui.this, mp1), 0,500);
            		
            	}
            };
        });
        
        stopMusic=new JButton();
        stopMusic.setText("Stop");
        stopMusic.setBounds(197, 123, 186, 30);
        stopMusic.setFont(boldCourier3);
        stopMusic.setEnabled(false);
        con.add(stopMusic);
        stopMusic.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed (ActionEvent e){
            	musicNow.setText("No Opened File");
            	playing=false;
            	if(tsp.a.size()>=1){
            		String[] bb=tsp.a.keySet().toArray(new String[1]);
            		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
            		for(String bbb : bb){
            			System.out.println("Send 'stop' to "+bbb);
            			noti.setText("Send 'stop' to "+bbb);
            			tsp.a.get(bbb).write("SC stop");
            		}
            	}
                if(mp1!=null){
                	mp1.close();
                }
                t0.cancel();
                playMusic.setText("Play");
                timeNow.setText("0:00");
            	timeAll.setText("0:00");
    			progress.setMaximum(60);
    			progress.setValue(0);
    			progress.setEnabled(false);
    			playMusic.setEnabled(false);
    			stopMusic.setEnabled(false);
    			resendMusic.setEnabled(false);
            };  
        });
        
        openMusic=new JButton();
        openMusic.setText("Open");
        openMusic.setBounds(10, 155, 186, 30);
        openMusic.setFont(boldCourier3);
        con.add(openMusic);
        openMusic.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed (ActionEvent e){
            	openMusic.setEnabled(false);
            	JFileChooser chooser = new JFileChooser();
            	chooser.setCurrentDirectory(new File(musicFilePath));
            	chooser.setAcceptAllFileFilterUsed(false);
                chooser.addChoosableFileFilter(new MusicFileFilter());
                
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                	playing=false;
                	noti.setText("Loading and Sending File");
                	System.out.println("You chose to open this file: " +
                			chooser.getSelectedFile().getAbsolutePath()) ;
                	musicFilePath=chooser.getSelectedFile().getAbsolutePath();
                	
                	musicNow.setText(chooser.getSelectedFile().getName());
                    if(mp1!=null){
                    	mp1.close();
                    }
                    t0.cancel();
                    playMusic.setText("Play");
                    timeNow.setText("0:00");
                	timeAll.setText("0:00");
        			progress.setMaximum(60);
        			progress.setValue(0);
        			progress.setEnabled(false);
        			playMusic.setEnabled(false);
        			stopMusic.setEnabled(false);
        			resendMusic.setEnabled(false);
                	if(tsp.a.size()>=1){
                		String[] bb=tsp.a.keySet().toArray(new String[1]);
                		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
                		for(String bbb : bb){
                			System.out.println("Tried to send music to "+bbb);
                			noti.setText("Tried to send music to "+bbb);
                			tsp.a.get(bbb).write("SC open "+musicFilePath);
                		}
                	}
                	mp1=new MusicPlay();
                	MusicInitThread mit=new MusicInitThread(mp1,musicFilePath,PodGui.this);
                	Thread mitt=new Thread(mit);
                	mitt.setName("Music Init Thread");
                	mitt.start();
                   //openMusic.setEnabled(true);
                }else{
                	noti.setText("No file opened.");
                	openMusic.setEnabled(true);
                }
            };  
        });
        
        resendMusic=new JButton();
        resendMusic.setText("Resend");
        resendMusic.setBounds(197, 155, 186, 30);
        resendMusic.setFont(boldCourier3);
        resendMusic.setEnabled(false);
        con.add(resendMusic);
        resendMusic.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed (ActionEvent e){
            	if(tsp.a.size()>=1){
            		String[] bb=tsp.a.keySet().toArray(new String[1]);
            		System.out.println(tsp.a.size() +" "+tsp.a.keySet().size()+" " +bb.length);
            		for(String bbb : bb){
            			System.out.println("Tried to send music to "+bbb);
            			tsp.a.get(bbb).write("SC open "+musicFilePath);
            		}
            	}
            };  
        });
        
        noti=new JLabel();
        noti.setText("Welcome to use Yoilk.");
        noti.setFont(boldCourier4);
        noti.setBounds(5, 187, 385, 18);
        noti.setHorizontalAlignment(JLabel.CENTER);
        noti.setVerticalAlignment(JLabel.CENTER);
        con.add(noti);
        
        jf.setVisible(true);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setResizable(false); 
	}
}
