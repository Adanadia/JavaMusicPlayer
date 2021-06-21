package com.lrc.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.lrc.lrc.LRC;
import com.lrc.lrc.Lyrics;
import com.lrc.tar.impl.time.Lyric;
import com.lrc.tar.impl.time.TimeTar;
import com.lrc.util.Util;

public class ParseLRC {
	public static Lyrics parseLRC(LRC lrc) {
		Lyrics lyrics = new Lyrics();
		lyrics.setAl(lrc.getAl().getValue());
		lyrics.setAr(lrc.getAr().getValue());
		lyrics.setBy(lrc.getBy().getValue());
		try {
			lyrics.setOffset(Integer.parseInt(lrc.getOffset().getValue()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		lyrics.setTi(lrc.getTi().getValue());
		lyrics.setLyrics(timeTarToLyric(lrc.getTimeTars()));
		return lyrics;
	}

	static List<Lyric> timeTarToLyric(List<TimeTar> timeTars) {
		List<Lyric> ls = new ArrayList<Lyric>();
		for (TimeTar t : timeTars) {
			long time = Util.timeToMillis(t.getValue().substring(1, t.getValue().length() - 1));
			Lyric l = new Lyric();
			l.setCurrent(time);
			l.setTxt(t.getText());
			l.setCurrentTimeStr(t.getValue().substring(1, t.getValue().length() - 1));
			ls.add(l);
		}
		Collections.sort(ls);
		Util.addTimeSize(ls);
		return ls;
	}
}
