package com.leward.cubbyhole.graphaware;

import java.io.Serializable;

public class SimpleObject implements Serializable {

	private static final long serialVersionUID = -8116656784087048204L;

	private String key1 = "test";
	
	private Long key2 = 5L;

	public SimpleObject() {
	}
	
	public String getKey1() {
		return key1;
	}

	public void setKey1(String key1) {
		this.key1 = key1;
	}

	public Long getKey2() {
		return key2;
	}

	public void setKey2(Long key2) {
		this.key2 = key2;
	}
	
}
