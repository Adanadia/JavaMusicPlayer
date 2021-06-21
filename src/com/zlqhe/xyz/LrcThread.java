package com.zlqhe.xyz;

import com.lrc.io.ParseLRC;
import com.lrc.io.ReadLRC;
import com.lrc.lrc.LRC;
import com.lrc.lrc.Lyrics;
import com.lrc.tar.impl.time.Lyric;

import javax.swing.*;

public class LrcThread implements Runnable{
    private LRC lrc;
    private Lyrics ls;
    JTextPane lrcLyrics;
    public LrcThread(String load,JTextPane lrcLyrics){
        lrc= ReadLRC.readLRC(load);
        ls= ParseLRC.parseLRC(lrc);
        this.lrcLyrics=lrcLyrics;
    }
    @Override
    public void run() {
        synchronized (Thread.currentThread()) {
            for (Lyric l : ls.getLyrics()) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                lrcLyrics.setText(l.getTxt());
                try {
                    Thread.sleep(l.getTimeSize());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
