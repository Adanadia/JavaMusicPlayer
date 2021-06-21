package com.lrc.test;

import com.lrc.io.ParseLRC;
import com.lrc.io.ReadLRC;
import com.lrc.lrc.LRC;
import com.lrc.lrc.Lyrics;
import com.lrc.tar.impl.time.Lyric;


public class Test {
	public static void main(String[] args) throws InterruptedException {
		LRC lrc = ReadLRC.readLRC("Main\\lrc\\好好.lrc");
		Lyrics ls = ParseLRC.parseLRC(lrc);
		playTest(ls);
	}

	static void playTest(Lyrics ls) throws InterruptedException {
		System.out.println("艺术家：" + ls.getAr());
		System.out.println("专辑：" + ls.getAl());
		System.out.println("歌曲：" + ls.getTi());
		System.out.println("歌词制作：" + ls.getBy());
		// Thread.sleep(ls.getOffset());//时间补偿暂未处理
		for (Lyric l : ls.getLyrics()) {
			System.out.println("时间:" + l.getCurrentTimeStr() + "    " + "lrc:" + l.getTxt());
			// System.out.println(l.getTxt());
			Thread.sleep(l.getTimeSize());
		}
	}
}
