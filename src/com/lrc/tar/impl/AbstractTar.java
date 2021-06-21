package com.lrc.tar.impl;

import com.lrc.tar.Tar;

public abstract class AbstractTar implements Tar{
	private String value;
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void setValue(String value) {
		// TODO Auto-generated method stub
		this.value=value;
	}
	
	@Override
	public abstract String getName();

}
