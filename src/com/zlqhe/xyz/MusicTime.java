package com.zlqhe.xyz;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicTime {
    private String path;
    private File file;
    private Clip clip;
    private AudioInputStream ais;
    public MusicTime(String path){
        this.path=path;
        file = new File(path);
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            ais = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public int havTime() {
        try {
            clip.open(ais);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        int time = (int) (clip.getMicrosecondLength() / 1000000D);
        return time;
    }
}
