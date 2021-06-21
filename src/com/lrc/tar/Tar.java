package com.lrc.tar;
/**
 * 标签
 * @author asus
 *
 */
public interface Tar {
	/**
	 * 得标签值
	 * @return
	 */
	public String getValue();
	/**
	 * 设置标签值
	 * @param value
	 */
	public void setValue(String value);
	/**
	 * 得标签名
	 * @return
	 */
	public abstract String getName();
}
