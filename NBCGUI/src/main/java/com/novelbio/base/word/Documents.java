package com.novelbio.base.word;

import com.jacob.com.Dispatch;



public class Documents{
	
	private Dispatch instance;

	public Documents(Dispatch instance) {
		this.instance = instance;
	}

	public Dispatch getInstance() {
		return instance;
	}

	public void setInstance(Dispatch instance) {
		this.instance = instance;
	}
}