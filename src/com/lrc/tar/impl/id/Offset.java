package com.lrc.tar.impl.id;

import com.lrc.tar.impl.AbstractTar;

/**
 * 时间补偿
 * <p> 其单位是毫秒，正值表示整体提前，负值相反。这是用于总体调整显示快慢的。</p>
 * @author asus
 *
 */
public class Offset extends AbstractTar{
	private final String name="offset";
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
