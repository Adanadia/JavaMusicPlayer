package com.lrc.lrc;

import java.util.List;

import com.lrc.tar.impl.id.Al;
import com.lrc.tar.impl.id.Ar;
import com.lrc.tar.impl.id.By;
import com.lrc.tar.impl.id.Offset;
import com.lrc.tar.impl.id.Ti;
import com.lrc.tar.impl.time.TimeTar;
/**
 * 基于标签，面相底层的。
 * @author asus
 *
 */
public class LRC {
	private Al al;
	private Ar ar;
	private By by;
	private Offset offset;
	private Ti ti;
	private List<TimeTar> timeTars;
	public Al getAl() {
		return al;
	}
	public void setAl(Al al) {
		this.al = al;
	}
	public Ar getAr() {
		return ar;
	}
	public void setAr(Ar ar) {
		this.ar = ar;
	}
	public By getBy() {
		return by;
	}
	public void setBy(By by) {
		this.by = by;
	}
	public Offset getOffset() {
		return offset;
	}
	public void setOffset(Offset offset) {
		this.offset = offset;
	}
	public Ti getTi() {
		return ti;
	}
	public void setTi(Ti ti) {
		this.ti = ti;
	}
	public List<TimeTar> getTimeTars() {
		return timeTars;
	}
	public void setTimeTars(List<TimeTar> timeTars) {
		this.timeTars = timeTars;
	}
}
