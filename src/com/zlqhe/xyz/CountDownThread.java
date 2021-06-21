package com.zlqhe.xyz;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CountDownThread implements Runnable{
    private String path;
    private JTextPane timePlne;
    public File file;
    public Clip clip;
    public AudioInputStream ais;
    public JButton button;
    public CountDownThread(String path,JTextPane timePlne,JButton button) throws Exception{
        this.path=path;
        this.timePlne=timePlne;
        this.button=button;
        file = new File(path);
        clip = AudioSystem.getClip();
        ais = AudioSystem.getAudioInputStream(file);
    }
    @Override
    public void run() {
        synchronized (Thread.currentThread()){
            try {
                clip.open(ais);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            int time=(int)(clip.getMicrosecondLength() / 1000000D);
            while(true){
                //System.out.println(formatTime(time));
                timePlne.setText(formatTime(time--));
                if(time==0){
                    button.doClick();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static String formatTime(int time){
        int minute=time/60;
        int second=(time-minute*60);
        if(second<10){
            return "0"+minute+":0"+second;
        }else{
            return "0"+minute+":"+second;
        }
    }
}
